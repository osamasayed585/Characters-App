package com.yassir.data.di


import com.yassir.common.di.DispatcherProvider
import com.yassir.data.remote.ArticlesService
import com.yassir.data.repository.articles.CharactersRepositoryImp
import com.yassir.data.repository.details.ArticleDetailsRepositoryImp
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.domain.repository.CharactersRepository
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
    fun provideArticlesRepository(
        articlesService: ArticlesService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler
    ): CharactersRepository {
        return CharactersRepositoryImp(
            apiService = articlesService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

    @Provides
    @Singleton
    fun provideArticleDetailsRepository(
        articlesService: ArticlesService,
        preferences: LocalDataStore,
        dispatcherProvider: DispatcherProvider,
        errorHandler: ErrorHandler
    ): CharacterDetailsRepository {
        return ArticleDetailsRepositoryImp(
            apiService = articlesService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

}