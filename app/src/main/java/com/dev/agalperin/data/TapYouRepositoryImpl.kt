package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.TapYouApi
import javax.inject.Inject

class TapYouRepositoryImpl @Inject constructor(private val tapYouApi: TapYouApi) : TapYouRepository {

    override suspend fun getPointsFromApi(count: Int): List<Point> {
        return tapYouApi.getPoints(count).pointsList.map { it.toPoint() }
    }
}