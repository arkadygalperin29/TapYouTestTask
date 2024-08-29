package com.dev.agalperin.data

import com.dev.agalperin.domain.TapYouRepositoryTest
import com.dev.agalperin.domain.model.PointTest
import com.dev.agalperin.domain.model.PointsResponseTest

class FakeTapYouRepositoryImpl : TapYouRepositoryTest {
    override suspend fun getPointsFromApi(count: Int): PointsResponseTest<PointTest> {
        return PointsResponseTest(
            listOf(
                PointTest(x = 5.0, y = 5.0),
                PointTest(x = 5.0, y = 7.0),
                PointTest(x = 8.0, y = 4.0),
                PointTest(x = 10.0, y = 7.0),
                PointTest(x = 13.0, y = 11.0),
            )
        )
    }
}