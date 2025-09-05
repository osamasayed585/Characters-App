package com.yassir.network.di.interceptors

import com.yassir.datastore.LocalDataStore
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CacheInterceptor(private val preferences: LocalDataStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response = runBlocking {
        // Fetch token and language from preferences
        val token = preferences.requestToken().firstOrNull()

        // Build the request with headers
        val requestBuilder = chain.request().newBuilder().apply {
            token?.let { addHeader("Authorization", "Bearer $it") }
            addHeader("Accept", "application/json")
        }

        // Log token
        Timber.d(" -> token: $token")

        // Proceed with the request
        val response = chain.proceed(requestBuilder.build())

        // Build the response with Cache-Control header
        val cacheControl = CacheControl.Builder()
            .maxAge(30, TimeUnit.MINUTES)
            .build()

        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}

