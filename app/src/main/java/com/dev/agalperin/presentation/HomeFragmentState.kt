package com.dev.agalperin.presentation

import com.dev.agalperin.domain.model.Point

data class HomeFragmentState(
    val points: List<Point> = emptyList(),
    val isLoading: Boolean = false
)