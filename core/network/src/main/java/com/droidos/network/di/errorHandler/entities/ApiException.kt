package com.droidos.network.di.errorHandler.entities

class ApiException(val error: ErrorEntity) : Throwable()
