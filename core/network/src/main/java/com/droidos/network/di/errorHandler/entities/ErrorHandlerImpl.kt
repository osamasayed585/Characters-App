package com.droidos.network.di.errorHandler.entities

import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.HttpURLConnection
import javax.inject.Inject

class ErrorHandlerImpl @Inject constructor() : ErrorHandler {

    override fun invoke(throwable: Throwable): ErrorEntity {
        return when (throwable) {
            is IOException -> ErrorEntity.Network

            is IllegalArgumentException -> ErrorEntity.Unknown(throwable.message)

            is HttpException -> handleHttpException(throwable)

            else -> ErrorEntity.Unknown(throwable.message)
        }
    }

    private fun handleHttpException(throwable: HttpException) = when (throwable.code()) {

        HttpURLConnection.HTTP_NOT_FOUND -> ErrorEntity.NotFound

        HttpURLConnection.HTTP_BAD_REQUEST -> ErrorEntity.BadRequest

        HttpURLConnection.HTTP_FORBIDDEN -> ErrorEntity.AccessDenied

        HttpURLConnection.HTTP_UNAVAILABLE -> ErrorEntity.ServiceUnavailable

        HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> ErrorEntity.Timeout

        HttpURLConnection.HTTP_UNAUTHORIZED -> ErrorEntity.Unauthorized

        HttpURLConnection.HTTP_BAD_GATEWAY -> ErrorEntity.GatewayError

        HttpURLConnection.HTTP_CLIENT_TIMEOUT -> ErrorEntity.Timeout

        HttpURLConnection.HTTP_INTERNAL_ERROR -> ErrorEntity.ServerInternalError

        else -> extractErrorDetails(throwable)

    }

    private fun extractErrorDetails(exception: HttpException): ErrorEntity.Unknown {
        return try {
            when (val response = exception.response()) {
                null -> ErrorEntity.Unknown(exception.message ?: "Null response")
                else -> handleResponseError(exception, response)
            }
        } catch (e: Exception) {
            ErrorEntity.Unknown(exception.message ?: "HTTP ${exception.code()} error")
        }
    }

    private fun handleResponseError(exception: HttpException, response: Response<*>): ErrorEntity.Unknown {
        return when (val errorBody = response.errorBody()) {
            null -> ErrorEntity.Unknown("HTTP ${exception.code()} Null response error body")
            else -> processErrorBody(exception, errorBody)
        }
    }

    private fun processErrorBody(exception: HttpException, errorBody: ResponseBody): ErrorEntity.Unknown {
        return try {
            val bodyString = errorBody.string()
            when {
                bodyString.isBlank() -> ErrorEntity.Unknown("HTTP ${exception.code()} error")
                else -> ErrorEntity.Unknown(bodyString.let {
                        if (it.contains("message")) extractionErrorMessage(it)
                        else it
                    })
            }
        } catch (e: Exception) {
            ErrorEntity.Unknown(exception.message ?: "HTTP ${exception.code()} error")
        }
    }

    private fun extractionErrorMessage(error: String): String {
        return try {
            JSONObject(error).optString("message", error)
        } catch (e: Exception) {
            error
        }
    }

}