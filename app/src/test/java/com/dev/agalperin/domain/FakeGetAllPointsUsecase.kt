package com.dev.agalperin.domain

import com.dev.agalperin.domain.model.PointTest

class FakeGetAllPointsUsecase(private val repository: TapYouRepositoryTest) {

    suspend fun execute(count: Int): List<PointTest> {
        return repository.getPointsFromApi(count).pointsList.sortedBy { it.x }
    }
}