package com.droidos.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.droidos.common.utils.Constants.PREFERENCES_STORE_NAME
import com.droidos.datastore.LocalDataStore
import com.droidos.datastore.LocalDataStoreImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDataModule {

    @Singleton
    @Provides
    fun provideLocalDataStore(@ApplicationContext app: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { app.preferencesDataStoreFile(PREFERENCES_STORE_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataSource(dataStore: DataStore<Preferences>): LocalDataStore {
        return LocalDataStoreImp(dataStore)
    }
}
