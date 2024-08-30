package com.dev.agalperin.presentation

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.utils.ErrorType
import com.dev.core.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPointsUsecase: Provider<GetAllPointsUsecase>,
    private val dispatchers: AppDispatchers,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(HomeScreenState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeScreenEffect>()
    val effects = _effects.asSharedFlow()

    fun getAllPoints(points: Int) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            getAllPointsUsecase.get().execute(points)
                .onSuccess { resultPoints ->
                    val comparedPoints = resultPoints.sortedBy { point -> point.x}
                    _state.update { currentState ->
                        currentState.copy(
                            points = comparedPoints
                        )
                    }
                }.onFailure { throwable ->
                    handleError(throwable)
                }
            _state.update { it.copy(isLoading = false) }
        }
    }

    // Function to save the chart image
    fun saveChartImage(coordinatesChart: View) {
        viewModelScope.launch(dispatchers.default) {
            val result = runCatching {
                val bitmap = createBitmapFromView(coordinatesChart)
                val filename = "chart_image_${System.currentTimeMillis()}.png"
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    saveImageToStorageAboveQ(bitmap, filename)
                } else {
                    saveImageToStorageBelowQ(bitmap, filename)
                }
            }

            result.onSuccess { uri ->
                uri?.let {
                    withContext(dispatchers.main) {
                        _effects.emit(HomeScreenEffect.ShowImageSavedSnackbar(it))
                    }
                }
            }.onFailure { throwable ->
                withContext(dispatchers.main) {
                    handleError(throwable)
                }
            }
        }
    }

    private fun createBitmapFromView(view: View): Bitmap {
        return Bitmap.createBitmap(
            view.width,
            view.height,
            Bitmap.Config.ARGB_8888
        ).apply {
            val canvas = Canvas(this)
            view.draw(canvas)
        }
    }

    private fun saveImageToStorageAboveQ(bitmap: Bitmap, filename: String): Uri? {
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val uri = context.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )

        uri?.let {
            context.contentResolver.openOutputStream(it).use { outputStream ->
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
            }
        }
        return uri
    }

    private fun saveImageToStorageBelowQ(bitmap: Bitmap, filename: String): Uri? {
        val picturesDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val imageFile = File(picturesDir, filename)
        FileOutputStream(imageFile).use { outputStream ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        }
        return Uri.fromFile(imageFile)
    }


    private suspend fun handleError(throwable: Throwable) {
        when (throwable) {
            is HttpException -> {
                val errorMessage = throwable.response()?.errorBody()?.string()
                _effects.emit(HomeScreenEffect.ShowError(ErrorType.HttpError(errorMessage)))
            }
            else -> {
                _effects.emit(HomeScreenEffect.ShowError(ErrorType.UndefinedError(throwable)))
            }
        }
    }
}


