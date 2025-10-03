package com.droidos.network.di.errorHandler.entities

interface ErrorHandler {
    operator fun invoke(throwable: Throwable): ErrorEntity
}