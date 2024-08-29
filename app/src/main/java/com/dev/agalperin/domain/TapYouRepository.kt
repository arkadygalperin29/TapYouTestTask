package com.dev.agalperin.domain

import com.dev.agalperin.domain.model.Point

interface TapYouRepository {
    suspend fun getPointsFromApi(count: Int): List<Point>
}