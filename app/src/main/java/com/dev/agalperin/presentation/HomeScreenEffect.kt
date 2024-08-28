package com.dev.agalperin.presentation

sealed class HomeScreenEffect {
    data class ShowError(val error: Throwable): HomeScreenEffect()
}