package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepository
import com.dev.tapyouapi.TapYouApi
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject

class TapYouRepositoryImpl @Inject constructor(private val tapYouApi: TapYouApi) : TapYouRepository {

    override fun getPointsFromApi(count: Int): Flow<PointsResponse<PointDto>> {
        return flow {
            val response = tapYouApi.getPoints(count)
            emit(response)
        }
    }
}