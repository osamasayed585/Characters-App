package com.yassir.network.di.errorHandler

import com.yassir.common.di.DispatcherProvider
import com.yassir.network.di.errorHandler.entities.ApiException
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

suspend inline fun <T, R> safeApiCall(
    dispatcher: DispatcherProvider,
    errorHandler: ErrorHandler,
    crossinline apiCall: suspend () -> Response<BaseResponse<T>>,
    crossinline apiResultOf: suspend (T) -> Result<R>
): Result<R> = withContext(dispatcher.io) {
    runCatching {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body() ?: throw IllegalArgumentException("Null body from API")
            val status =
                ApiStatus.entries.firstOrNull { it.value == body.status } ?: ApiStatus.ServerError

            when (status) {
                ApiStatus.Success -> {
                    body.data?.let { apiResultOf(it) }
                        ?: throw IllegalArgumentException("Null data from API")
                }

                ApiStatus.ServerError -> {
                    throw IllegalArgumentException(body.message)
                }
            }

        } else throw HttpException(response)

    }.getOrElse { throwable ->
        val msg = errorHandler.invoke(throwable)
        Result.failure(ApiException(msg))
    }
}

enum class ApiStatus(val value: String) {
    Success("ok"),
    ServerError("error")
}