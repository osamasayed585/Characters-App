package com.droidos.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.droidos.common.di.DispatcherProvider
import com.droidos.common.utils.Constants
import com.droidos.data.mapper.asExternalUiModel
import com.droidos.data.remote.CharactersService
import com.droidos.datastore.LocalDataStore
import com.droidos.domain.repository.SearchCharacterRepository
import com.droidos.model.beans.CharacterDto
import com.droidos.model.beans.CharacterUIModel
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchCharacterImp @Inject constructor(
    private val apiService: CharactersService,
    private val preferences: LocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandler: ErrorHandler,
) : SearchCharacterRepository {


    override suspend fun searchCharacters(name: String): Flow<PagingData<CharacterUIModel>> {
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