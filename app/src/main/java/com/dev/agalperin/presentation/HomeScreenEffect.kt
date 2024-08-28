package com.dev.agalperin.presentation

import com.dev.agalperin.utils.ErrorType

sealed class HomeScreenEffect {
    data class ShowError(val error: ErrorType): HomeScreenEffect()
}

