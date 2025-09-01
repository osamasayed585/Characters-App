package com.yassir.datastore

import com.yassir.common.Language
import kotlinx.coroutines.flow.Flow

interface LocalDataStore {
    suspend fun saveToken(accessToken: String)
    suspend fun requestToken(): Flow<String?>
}