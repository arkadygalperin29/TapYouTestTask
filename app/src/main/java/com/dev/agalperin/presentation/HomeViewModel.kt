package com.dev.agalperin.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.agalperin.domain.GetAllPointsUsecase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class HomeViewModel @Inject constructor(private val getAllPointsUsecase: Provider<GetAllPointsUsecase>) : ViewModel() {

    private val _state = MutableStateFlow(HomeFragmentState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<HomeScreenEffect>()
    val effects = _effects.asSharedFlow()

    fun getAllPoints(points: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                getAllPointsUsecase.get().execute(points).collect { result ->
                    _state.update { currentState ->
                        currentState.copy(
                            points = result
                        )
                    }
                }
            } catch (e: Exception) {
                _effects.emit(HomeScreenEffect.ShowError(e))
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}

