package com.yassir.network.di.errorHandler

import android.graphics.pdf.PdfDocument.PageInfo
import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(
    @SerializedName("results")
    val data: T?,
    val info: PageInfo,
)
