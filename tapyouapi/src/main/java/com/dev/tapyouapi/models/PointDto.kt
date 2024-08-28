package com.dev.tapyouapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointDto(
    @SerialName("x")
    val x: Double,
    @SerialName("y")
    val y: Double
)
