package com.dev.agalperin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.agalperin.domain.GetAllPointsUsecase
import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

data class HomeFragmentState(
    val points: List<Point> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllPointsUsecase: Provider<GetAllPointsUsecase>) :
    ViewModel() {

    private val _state = MutableStateFlow(HomeFragmentState())
    val state = _state.asStateFlow()

    fun getAllPoints(points: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }

            getAllPointsUsecase.get().execute(points)
                .collect { result ->
                    _state.update { currentState ->
                        result.fold(
                            onSuccess = { points ->
                                currentState.copy(points = points, isLoading = false)
                            },
                            onFailure = {
                                currentState.copy(isLoading = false)
                            }
                        )
                    }
                }
        }
    }
}