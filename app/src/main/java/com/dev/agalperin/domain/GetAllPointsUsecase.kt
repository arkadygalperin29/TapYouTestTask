package com.dev.agalperin.domain

import com.dev.agalperin.domain.model.Point
import com.dev.agalperin.utils.toPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllPointsUsecase @Inject constructor(private val repository: TapYouRepository) {

    fun execute(count: Int): Flow<Result<List<Point>>> {
        return repository.getPointsFromApi(count).map { result ->
            result.map { points ->
                points.pointsList.map { it.toPoint() }
            }
        }
    }
}