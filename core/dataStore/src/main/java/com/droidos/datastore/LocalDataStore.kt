package com.droidos.datastore

import kotlinx.coroutines.flow.Flow

interface LocalDataStore {
    suspend fun saveToken(accessToken: String)
    suspend fun requestToken(): Flow<String?>
}