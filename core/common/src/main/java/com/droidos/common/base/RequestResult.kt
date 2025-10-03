package com.droidos.common.base

sealed class RequestResult<out T> {
    data class Success<out T>(val data: T) : RequestResult<T>()

    data class Error(val message: String) : RequestResult<Nothing>()
}