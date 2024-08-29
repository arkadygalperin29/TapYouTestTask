package com.dev.agalperin.data

import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.models.PointDto

fun PointDto.toPoint(): Point {
    return Point(
        x = x.toFloat(),
        y = y.toFloat()
    )
}