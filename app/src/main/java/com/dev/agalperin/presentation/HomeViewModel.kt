package com.dev.agalperin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.utils.ErrorType
import com.dev.core.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllPointsUsecase: Provider<GetAllPointsUsecase>,
    private val dispatchers: AppDispatchers
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


