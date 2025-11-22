package com.droidos.network.di.errorHandler

import com.droidos.common.di.DispatcherProvider
import com.droidos.network.di.errorHandler.entities.ApiException
import com.droidos.network.di.errorHandler.entities.ErrorEntity
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

suspend inline fun <T, R> safeApiCall(
    dispatcher: DispatcherProvider,
    errorHandler: ErrorHandler,
    crossinline apiCall: suspend () -> Response<T>,
    crossinline apiResultOf: suspend (T) -> Result<R>,
): Result<R> =
    withContext(dispatcher.io) {
        runCatching {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body() ?: throw IllegalArgumentException("Null body from API")
                apiResultOf(body)
            } else {
                throw HttpException(response)
            }
        }.getOrElse { throwable ->
            throwable.printStackTrace()
            val msg: ErrorEntity = errorHandler.invoke(throwable)
            Result.failure(ApiException(msg))
        }
    }
