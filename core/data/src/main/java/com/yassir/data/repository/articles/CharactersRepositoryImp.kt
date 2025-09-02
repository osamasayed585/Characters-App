package com.yassir.data.repository.articles

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.data.mapper.asExternalUiModel
import com.yassir.data.remote.ArticlesService
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.CharactersRepository
import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CharactersRepositoryImp @Inject constructor(
    private val apiService: ArticlesService,
    private val preferences: LocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandler: ErrorHandler,
) : CharactersRepository {


    override fun requestCharacters(): Flow<PagingData<CharacterUIModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = Constants.PAGE_SIZE,
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2,
            ),
            pagingSourceFactory = {
                CharacterDataSource(
                    apiService = apiService,
                    errorHandler = errorHandler,
                    dispatcherProvider = dispatcherProvider
                )
            },
        )
            .flow
            .map { pagingData: PagingData<CharacterDto> ->
                pagingData.map { networkArticle: CharacterDto ->
                    networkArticle.asExternalUiModel()
                }
            }
    }
}
