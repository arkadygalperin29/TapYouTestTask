package com.dev.agalperin.utils


sealed class ErrorType {
    data class HttpError(val message: String?) : ErrorType()
    data class UndefinedError(val error: Throwable) : ErrorType()
}