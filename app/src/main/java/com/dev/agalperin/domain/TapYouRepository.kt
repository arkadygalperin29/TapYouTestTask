package com.dev.agalperin.domain

import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.flow.Flow

interface TapYouRepository {
    fun getPointsFromApi(count: Int): Flow<Result<PointsResponse<PointDto>>>
}