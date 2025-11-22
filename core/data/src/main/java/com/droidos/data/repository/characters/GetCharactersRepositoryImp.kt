package com.droidos.data.repository.characters

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.droidos.common.di.DispatcherProvider
import com.droidos.common.utils.Constants
import com.droidos.data.mapper.asExternalUiModel
import com.droidos.data.remote.CharactersService
import com.droidos.datastore.LocalDataStore
import com.droidos.domain.repository.GetCharactersRepository
import com.droidos.model.beans.CharacterDto
import com.droidos.model.beans.CharacterUIModel
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCharactersRepositoryImp
    @Inject
    constructor(
        private val apiService: CharactersService,
        private val preferences: LocalDataStore,
        private val dispatcherProvider: DispatcherProvider,
        private val errorHandler: ErrorHandler,
    ) : GetCharactersRepository {
        override fun fetchCharacters(): Flow<PagingData<CharacterUIModel>> =
            Pager(
                config =
                    PagingConfig(
                        initialLoadSize = Constants.PAGE_SIZE,
                        pageSize = Constants.PAGE_SIZE,
                        enablePlaceholders = false,
                        prefetchDistance = 2,
                    ),
                pagingSourceFactory = {
                    CharacterDataSource(
                        apiService = apiService,
                        errorHandler = errorHandler,
                        dispatcherProvider = dispatcherProvider,
                    )
                },
            ).flow
                .map { pagingData: PagingData<CharacterDto> ->
                    pagingData.map { networkCharacter: CharacterDto ->
                        networkCharacter.asExternalUiModel()
                    }
                }
    }
