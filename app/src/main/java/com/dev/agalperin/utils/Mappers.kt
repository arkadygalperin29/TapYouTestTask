package com.dev.agalperin.utils

import com.dev.agalperin.domain.model.Point
import com.dev.tapyouapi.models.PointDto

fun PointDto.toPoint(): Point {
    return Point(
        x = x,
        y = y
    )
}