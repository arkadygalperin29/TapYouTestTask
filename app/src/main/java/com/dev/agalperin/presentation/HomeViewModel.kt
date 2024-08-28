package com.dev.agalperin.presentation

import android.annotation.SuppressLint
import android.net.http.HttpException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

data class HomeFragmentState(
    val points: List<Point> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllPointsUsecase: Provider<GetAllPointsUsecase>) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeFragmentState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeScreenEffect>()
    val effects = _effects.asSharedFlow()



    fun getAllPoints(points: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, errorMessage = null) }


            getAllPointsUsecase.get().execute(points).collect { result ->
                result.fold(
                    onSuccess = { points ->
                        _state.update { currentState ->
                            currentState.copy(
                                points = points,
                                isLoading = false
                            )
                        }
                    },
                    onFailure = { error ->
                        _effects.emit(HomeScreenEffect.ShowError(error))
                        _state.update { currentState ->
                            currentState.copy(
                                isLoading = false
                            )
                        }
                    }
                )
            }
        }
    }
}

sealed class HomeScreenEffect {
    data class ShowError(val error: Throwable): HomeScreenEffect()
}