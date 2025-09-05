package com.yassir.data.di

import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.domain.repository.GetCharactersRepository
import com.yassir.domain.repository.SearchCharacterRepository
import com.yassir.domain.useCases.CharactersUseCases
import com.yassir.domain.useCases.GetCharacterDetailsUseCase
import com.yassir.domain.useCases.GetCharacterUseCase
import com.yassir.domain.useCases.GetSearchUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideCharactersUseCase(getCharactersRepository: GetCharactersRepository): GetCharacterUseCase {
        return GetCharacterUseCase(getCharactersRepository)
    }

    @Provides
    @Singleton
    fun provideSearchCharacterUseCase(searchCharacterRepository: SearchCharacterRepository): GetSearchUseCase {
        return GetSearchUseCase(searchCharacterRepository)
    }

    @Provides
    @Singleton
    fun provideCharacterDetailsUseCase(characterDetailsRepository: CharacterDetailsRepository): GetCharacterDetailsUseCase {
        return GetCharacterDetailsUseCase(characterDetailsRepository)
    }

    @Provides
    @Singleton
    fun provideCharactersUseCases(
        charactersRepository: GetCharactersRepository,
        searchCharacterRepository: SearchCharacterRepository
    ): CharactersUseCases {
        return CharactersUseCases(
            getCharactersUseCase = GetCharacterUseCase(charactersRepository),
            getSearchUseCase = GetSearchUseCase(searchCharacterRepository)
        )
    }
}