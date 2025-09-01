package com.yassir.network.di.errorHandler.entities

class ApiException(val error: ErrorEntity) : Throwable()
