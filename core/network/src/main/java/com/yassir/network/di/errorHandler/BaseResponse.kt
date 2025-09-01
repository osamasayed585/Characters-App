package com.yassir.network.di.errorHandler

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    val status: String = "",
    @SerializedName("articles")
    val data: T?,
    val totalResults: Int = 0,
    val message: String,
)
