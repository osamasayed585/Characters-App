package com.droidos.network.di.errorHandler.entities

import MockTestData
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import java.io.IOException


class ErrorHandlerImplTest {

    private val errorHandler: ErrorHandler = ErrorHandlerImpl()

    @Test
    fun `IOException handling`() {
        assertEquals(ErrorEntity.Network, errorHandler(IOException()))
    }

    @Test
    fun `IllegalArgumentException handling`() {
        val message = "Illegal Argument Exception"
        assertEquals(ErrorEntity.Unknown(message), errorHandler(IllegalArgumentException(message)))
    }

    @Test
    fun `Generic Throwable handling`() {
        val message = "Runtime Exception"
        assertEquals(ErrorEntity.Unknown(message), errorHandler(RuntimeException(message)))
    }

    @Test
    fun `HttpExceptions with known status codes`() {
        listOf(
            400 to ErrorEntity.BadRequest,
            401 to ErrorEntity.Unauthorized,
            403 to ErrorEntity.AccessDenied,
            404 to ErrorEntity.NotFound,
            408 to ErrorEntity.Timeout,
            500 to ErrorEntity.ServerInternalError,
            502 to ErrorEntity.GatewayError,
            503 to ErrorEntity.ServiceUnavailable,
            504 to ErrorEntity.Timeout
        ).forEach { (code, expected) ->
            val response = MockTestData.getErrorResponses().getValue(code)
            val exception = HttpException(response)
            assertEquals(expected, errorHandler(exception))
        }
    }

    @Test
    fun `HttpException with unhandled status code and error body`() {
        val response = MockTestData.getErrorResponses()[520]!!
        val result = errorHandler(HttpException(response))
        assertTrue(result is ErrorEntity.Unknown)
        assertEquals("Unknown Error", (result as ErrorEntity.Unknown).message)
    }

    @Test
    fun `HttpException with handled status code`() {
        val response = MockTestData.getErrorResponses()[404]!!
        val result = errorHandler(HttpException(response))
        assertEquals(ErrorEntity.NotFound, result)
    }
}
