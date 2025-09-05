package com.yassir.data.repository.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.data.mapper.asExternalUiModel
import com.yassir.data.remote.CharactersService
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.SearchCharacterRepository
import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCharacterImp @Inject constructor(
    private val apiService: CharactersService,
    private val preferences: LocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandler: ErrorHandler,
) : SearchCharacterRepository {


    override suspend fun searchCharacter(name: String): Flow<PagingData<CharacterUIModel>> {
        return Pager(
            config = PagingConfig(
                initialLoadSize = Constants.PAGE_SIZE,
                pageSize = Constants.PAGE_SIZE,
                enablePlaceholders = false,
                prefetchDistance = 2,
            ),
            pagingSourceFactory = {
                SearchCharacterDataSource(
                    apiService = apiService,
                    errorHandler = errorHandler,
                    dispatcherProvider = dispatcherProvider,
                    name = name
                )
            },
        )
            .flow
            .map { pagingData: PagingData<CharacterDto> ->
                pagingData.map { networkCharacter: CharacterDto ->
                    networkCharacter.asExternalUiModel()
                }
            }
    }
}