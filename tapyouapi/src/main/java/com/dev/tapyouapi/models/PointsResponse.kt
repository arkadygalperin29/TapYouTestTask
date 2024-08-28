package com.dev.tapyouapi.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PointsResponse<E>(
    @SerialName("points")
    val pointsList: List<E>
)