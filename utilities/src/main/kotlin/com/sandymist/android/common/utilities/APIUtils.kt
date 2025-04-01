package com.sandymist.android.common.utilities

import retrofit2.HttpException
import timber.log.Timber
import java.net.ConnectException
import java.net.UnknownHostException

suspend fun <T> apiRequest(tag: String, request: suspend () -> T): T {
    Timber.d("Processing API request, tag $tag")
    return try {
        request()
    } catch (exception: Exception) {
        Timber.e("Exception in apiRequest: ${exception.message}, tag $tag")
        when (exception) {
            is HttpException -> {
                when (exception.code()) {
                    400 -> {
                        Timber.e("Bad request")
                        throw AppException.NetworkException(
                            message = "Bad request",
                            cause = exception
                        )
                    }
                    401 -> {
                        Timber.e("Unauthorized")
                        throw AppException.UnauthorizedException(
                            message = "Unauthorized exception",
                            cause = exception,
                        )
                    }
                }
            }
            is ConnectException -> {
                throw AppException.NetworkException(message = "Connect error", cause = exception)
            }
            is UnknownHostException -> {
                throw AppException.NetworkException(message = "Unknown host", cause = exception)
            }
        }
        Timber.d("Unknown exception: ${exception.description}, tag $tag")
        throw AppException.NetworkException(message = "Unclassified error", cause = exception)
    }
}
