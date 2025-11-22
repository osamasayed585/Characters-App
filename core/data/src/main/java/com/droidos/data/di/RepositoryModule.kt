package com.droidos.data.di

import com.droidos.common.di.DispatcherProvider
import com.droidos.data.remote.CharactersService
import com.droidos.data.repository.characters.GetCharactersRepositoryImp
import com.droidos.data.repository.details.DetailsRepositoryImp
import com.droidos.data.repository.search.SearchCharacterImp
import com.droidos.datastore.LocalDataStore
import com.droidos.domain.repository.CharacterDetailsRepository
import com.droidos.domain.repository.GetCharactersRepository
import com.droidos.domain.repository.SearchCharacterRepository
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideSearchCharacterRepository(
        charactersService: CharactersService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler,
    ): SearchCharacterRepository =
        SearchCharacterImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler,
        )

    @Provides
    @Singleton
    fun provideCharactersRepository(
        charactersService: CharactersService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler,
    ): GetCharactersRepository =
        GetCharactersRepositoryImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler,
        )

    @Provides
    @Singleton
    fun provideCharacterDetailsRepository(
        charactersService: CharactersService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler,
    ): CharacterDetailsRepository =
        DetailsRepositoryImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler,
        )
}
