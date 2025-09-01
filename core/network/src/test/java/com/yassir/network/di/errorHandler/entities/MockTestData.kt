import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

class MockTestData {

    companion object {
        private val jsonMediaType = "application/json".toMediaType()

        fun getErrorResponses(): Map<Int, Response<Any>> {
            return mapOf(
                400 to createErrorResponse(400, "Bad Request"),
                401 to createErrorResponse(401, "Unauthorized"),
                403 to createErrorResponse(403, "Forbidden"),
                404 to createErrorResponse(404, "Not Found"),
                408 to createErrorResponse(408, "Request Timeout"),
                500 to createErrorResponse(500, "Internal Server Error"),
                502 to createErrorResponse(502, "Bad Gateway"),
                503 to createErrorResponse(503, "Service Unavailable"),
                504 to createErrorResponse(504, "Gateway Timeout"),
                520 to createErrorResponse(520, "Unknown Error")
            )
        }

        private fun createErrorResponse(code: Int, message: String): Response<Any> {
            return Response.error(code, message.toResponseBody(jsonMediaType))
        }
    }

    val nullErrorResponse: Response<Any> = Response.error(520, null)
    val emptyErrorResponse: Response<Any> = Response.error(520, "".toResponseBody(jsonMediaType))
    val errorResponse: Response<Any> = Response.error(520, "Invalid JSON".toResponseBody(jsonMediaType))
    val nullBodyErrorResponse: Response<Any> = Response.error(404, null)
}
