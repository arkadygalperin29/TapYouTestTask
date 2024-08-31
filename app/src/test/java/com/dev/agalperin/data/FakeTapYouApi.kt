package com.dev.agalperin.data

import com.dev.tapyouapi.TapYouApi
import com.dev.tapyouapi.models.PointDto
import com.dev.tapyouapi.models.PointsResponse

class FakeTapYouApi : TapYouApi {
    override suspend fun getPoints(amountOfDots: Int): PointsResponse<PointDto> {
        val fakePoints = listOf(
            PointDto(1.0, 2.0),
            PointDto(3.0, 4.0),
            PointDto(5.0, 6.0)
        ).take(amountOfDots)
        return PointsResponse(fakePoints)
    }
}