package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepository
import com.dev.agalperin.domain.TapYouRepositoryTest
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeTapYouRepositoryImpl : TapYouRepositoryTest {
    override fun getPointsFromApi(count: Int): Flow<PointsResponse<PointDto>> {
        return flow {
            emit(
                PointsResponse(
                    listOf(
                        PointDto(x = 5.0, y = 5.0),
                        PointDto(x = 5.0, y = 7.0),
                        PointDto(x = 8.0, y = 4.0),
                        PointDto(x = 10.0, y = 7.0),
                        PointDto(x = 13.0, y = 11.0),
                    )
                )
            )
        }
    }
}