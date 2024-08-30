package com.dev.agalperin.domain

import com.dev.agalperin.domain.model.Point
import javax.inject.Inject

class GetAllPointsUsecase @Inject constructor(private val repository: TapYouRepository) {

    suspend fun execute(count: Int): Result<List<Point>> {
        return repository.getPointsFromApi(count)
    }
}