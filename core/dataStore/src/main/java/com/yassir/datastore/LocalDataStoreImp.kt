package com.yassir.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class LocalDataStoreImp(
    private val dataStore: DataStore<Preferences>,
) : LocalDataStore {


    override suspend fun saveToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[TOKEN] = accessToken
        }
    }

    override suspend fun requestToken(): Flow<String?> {
        return dataStore.data.map { preferences -> preferences[TOKEN] ?: "9a95acef21be4858a910e63a25ca0a5a" }
    }


    companion object {
        private val TOKEN = stringPreferencesKey("key_token")
    }
}