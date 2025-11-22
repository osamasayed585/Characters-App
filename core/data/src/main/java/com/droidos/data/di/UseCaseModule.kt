package com.droidos.data.di

import com.droidos.domain.repository.CharacterDetailsRepository
import com.droidos.domain.repository.GetCharactersRepository
import com.droidos.domain.repository.SearchCharacterRepository
import com.droidos.domain.useCases.CharactersUseCases
import com.droidos.domain.useCases.GetCharacterDetailsUseCase
import com.droidos.domain.useCases.GetCharacterUseCase
import com.droidos.domain.useCases.GetSearchUseCase
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
    fun provideCharactersUseCase(getCharactersRepository: GetCharactersRepository): GetCharacterUseCase =
        GetCharacterUseCase(getCharactersRepository)

    @Provides
    @Singleton
    fun provideSearchCharacterUseCase(searchCharacterRepository: SearchCharacterRepository): GetSearchUseCase =
        GetSearchUseCase(searchCharacterRepository)

    @Provides
    @Singleton
    fun provideCharacterDetailsUseCase(characterDetailsRepository: CharacterDetailsRepository): GetCharacterDetailsUseCase =
        GetCharacterDetailsUseCase(characterDetailsRepository)

    @Provides
    @Singleton
    fun provideCharactersUseCases(
        charactersRepository: GetCharactersRepository,
        searchCharacterRepository: SearchCharacterRepository,
    ): CharactersUseCases =
        CharactersUseCases(
            getCharactersUseCase = GetCharacterUseCase(charactersRepository),
            getSearchUseCase = GetSearchUseCase(searchCharacterRepository),
        )
}
