package com.yassir.data.di

import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.domain.repository.CharactersRepository
import com.yassir.domain.useCases.GetCharacterDetailsUseCase
import com.yassir.domain.useCases.GetCharacterUseCase
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
    fun provideArticlesUseCase(charactersRepository: CharactersRepository): GetCharacterUseCase {
        return GetCharacterUseCase(charactersRepository)
    }

    @Provides
    @Singleton
    fun provideArticleDetailsUseCase(articleDetailsRepository: CharacterDetailsRepository): GetCharacterDetailsUseCase {
        return GetCharacterDetailsUseCase(articleDetailsRepository)
    }

}