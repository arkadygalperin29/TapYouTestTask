package com.dev.agalperin.utils

import retrofit2.HttpException

sealed class ErrorType {
    data class HttpError(val error: HttpException) : ErrorType()
    data class UndefinedError(val error: Throwable) : ErrorType()
}