@file:Suppress("unused")
package com.sandymist.android.common.utilities

sealed interface AppException {
    val description: String

    class BadRequestException(
        override val message: String? = null,
        override val cause: Throwable? = null,
        override val description: String = "Bad request", // TODO: use resource id
    ): Exception(cause), AppException

    class UnauthorizedException(
        override val message: String? = null,
        override val cause: Throwable? = null,
        override val description: String = "Unauthorized access",
    ): Exception(cause), AppException

    class NetworkException(
        override val message: String,
        override val cause: Throwable? = null,
        override val description: String = "Connection error",
    ): Exception(cause), AppException

    class DataException(
        override val message: String,
        override val cause: Throwable? = null,
        override val description: String = "Unexpected or missing data",
    ): Exception(cause), AppException
}

val Exception.description: String
    get() = when(this) {
        is AppException -> this.description
        else -> this.message ?: "Unknown error"
    }
