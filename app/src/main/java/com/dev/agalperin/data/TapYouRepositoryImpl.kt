package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.model.Point
import com.dev.core.AppDispatchers
import com.dev.tapyouapi.TapYouApi
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TapYouRepositoryImpl @Inject constructor(
    private val tapYouApi: TapYouApi,
    private val dispatchers: AppDispatchers
) : TapYouRepository {

    override suspend fun getPointsFromApi(count: Int): List<Point> {
        return withContext(dispatchers.io) {
            tapYouApi.getPoints(count).pointsList.map { it.toPoint() }
        }
    }
}