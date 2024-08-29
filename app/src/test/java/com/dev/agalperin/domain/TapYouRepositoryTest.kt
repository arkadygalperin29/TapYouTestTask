package com.dev.agalperin.domain

import com.dev.agalperin.domain.model.PointTest
import com.dev.agalperin.domain.model.PointsResponseTest

interface TapYouRepositoryTest{
    suspend fun getPointsFromApi(count: Int): PointsResponseTest<PointTest>
}