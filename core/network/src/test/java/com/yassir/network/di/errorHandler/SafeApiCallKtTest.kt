package com.yassir.network.di.errorHandler

import com.yassir.common.di.DispatcherProvider
import com.yassir.network.di.errorHandler.entities.ApiException
import com.yassir.network.di.errorHandler.entities.ErrorEntity
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import retrofit2.HttpException
import java.io.IOException

class SafeApiCallKtTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val mockDispatcherProvider = mockk<DispatcherProvider> {
        every { io } returns testDispatcher
    }
    private val mockErrorHandler = mockk<ErrorHandler>()
    private lateinit var mockApiCall: suspend () -> Response<BaseResponse<String>>
    private lateinit var mockApiResultOf: suspend (String) -> Result<Int>

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        mockApiCall = mockk()
        mockApiResultOf = mockk()
    }

    @Test
    fun `successful API call returns transformed result`() = testScope.runTest {
        // Given
        val testData = "test"
        val testResponse = Response.success(BaseResponse("ok", testData, 2, "Success"))
        val mockApiCall: suspend () -> Response<BaseResponse<String>> = { testResponse }
        val mockApiResultOf: suspend (String) -> Result<Int> = { Result.success(42) }

        // When
        val result = safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        assertEquals(42, result.getOrNull())
    }

    @Test
    fun `HTTP error calls error handler`() = testScope.runTest {
        // Given
        val errorResponse = Response.error<BaseResponse<String>>(
            404,
            "".toResponseBody("application/json".toMediaType())
        )
        val mockApiCall: suspend () -> Response<BaseResponse<String>> = { errorResponse }
        every { mockErrorHandler.invoke(any()) } returns ErrorEntity.NotFound

        // When
        val result = safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(ErrorEntity.NotFound, (it as ApiException).error)
        }
        verify { mockErrorHandler.invoke(any<HttpException>()) }
    }

    @Test
    fun `null response body throws IllegalArgumentException`() = testScope.runTest {
        // Given
        val expectedMessage = "Null body from API"
        val nullBodyResponse = Response.success<BaseResponse<String>>(null)
        val mockApiCall: suspend () -> Response<BaseResponse<String>> = { nullBodyResponse }

        every { mockErrorHandler.invoke(any()) } answers {
            when (val throwable = firstArg<Throwable>()) {
                is IllegalArgumentException -> ErrorEntity.Unknown(throwable.message ?: expectedMessage)
                else -> ErrorEntity.Unknown("Unexpected error")
            }
        }

        // When
        val result = safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        assertTrue(result.isFailure)
        result.onFailure { exception ->

            assertTrue(exception is ApiException)
            val errorEntity = (exception as ApiException).error

            assertTrue(errorEntity is ErrorEntity.Unknown)

            val body = (errorEntity as ErrorEntity.Unknown).message
            assertEquals(expectedMessage, body)
        }
    }


    @Test
    fun `server error status throws exception with message`() = testScope.runTest {
        // Given
        val errorMessage = "Server error"
        val errorResponse = Response.success(
            BaseResponse(
                status = "error",
                data = null,
                totalResults = 0,
                message = errorMessage
            )
        )
        val mockApiCall: suspend () -> Response<BaseResponse<Nothing>> = { errorResponse }

        // Mock the error handler to return an ErrorEntity with our message
        every { mockErrorHandler.invoke(any()) } answers {
            when (val throwable = firstArg<Throwable>()) {
                is IllegalArgumentException -> ErrorEntity.Unknown(throwable.message ?: errorMessage)
                else -> ErrorEntity.ServerInternalError
            }
        }

        // When
        val result = safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        assertTrue(result.isFailure)
        result.onFailure { exception ->
            when (exception) {
                is ApiException -> {
                    // Check if the error contains our message
                    when (val errorEntity = exception.error) {
                        is ErrorEntity.Unknown -> assertEquals(errorMessage, errorEntity.message)
                        else -> fail("Expected ErrorEntity.Unknown with message but got $errorEntity")
                    }
                }

                else -> fail("Expected ApiException but got ${exception::class.simpleName}")
            }
        }
    }

    @Test
    fun `IO exception is handled properly`() = testScope.runTest {
        // Given
        val mockApiCall: suspend () -> Response<BaseResponse<Nothing>> =
            { throw IOException("Network error") }
        every { mockErrorHandler.invoke(any()) } returns ErrorEntity.Network

        // When
        val result = safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        assertTrue(result.isFailure)
        result.onFailure {
            assertEquals(ErrorEntity.Network, (it as ApiException).error)
        }
    }

    @Test
    fun `runs on IO dispatcher`() = testScope.runTest {
        // Given
        val testData = "test"
        val testResponse = Response.success(BaseResponse("ok", testData, 200, "Success"))
        val mockApiCall: suspend () -> Response<BaseResponse<String>> = { testResponse }
        val mockApiResultOf: suspend (String) -> Result<Int> = { Result.success(42) }

        // When
        safeApiCall(
            mockDispatcherProvider,
            mockErrorHandler,
            mockApiCall,
            mockApiResultOf
        )

        // Then
        verify { mockDispatcherProvider.io }
    }
}