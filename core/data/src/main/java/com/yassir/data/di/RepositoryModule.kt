package com.yassir.data.di


import com.yassir.common.di.DispatcherProvider
import com.yassir.data.remote.CharactersService
import com.yassir.data.repository.characters.GetCharactersRepositoryImp
import com.yassir.data.repository.search.SearchCharacterImp
import com.yassir.data.repository.details.CharacterDetailsRepositoryImp
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.domain.repository.GetCharactersRepository
import com.yassir.domain.repository.SearchCharacterRepository
import com.yassir.network.di.errorHandler.entities.ErrorHandler
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
        errorHandler: ErrorHandler
    ): SearchCharacterRepository {
        return SearchCharacterImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

    @Provides
    @Singleton
    fun provideCharactersRepository(
        charactersService: CharactersService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler
    ): GetCharactersRepository {
        return GetCharactersRepositoryImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

    @Provides
    @Singleton
    fun provideCharacterDetailsRepository(
        charactersService: CharactersService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler
    ): CharacterDetailsRepository {
        return CharacterDetailsRepositoryImp(
            apiService = charactersService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

}