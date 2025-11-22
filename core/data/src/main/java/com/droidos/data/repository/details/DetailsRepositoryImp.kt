package com.droidos.data.repository.details

import com.droidos.common.di.DispatcherProvider
import com.droidos.data.mapper.asExternalUiModel
import com.droidos.data.remote.CharactersService
import com.droidos.datastore.LocalDataStore
import com.droidos.domain.repository.CharacterDetailsRepository
import com.droidos.model.beans.CharacterDto
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import com.droidos.network.di.errorHandler.safeApiCall
import javax.inject.Inject

class DetailsRepositoryImp
    @Inject
    constructor(
        private val apiService: CharactersService,
        private val preferences: LocalDataStore,
        private val dispatcherProvider: DispatcherProvider,
        private val errorHandler: ErrorHandler,
    ) : CharacterDetailsRepository {
        override suspend fun requestCharacterDetails(id: Int) =
            safeApiCall(
                errorHandler = errorHandler,
                dispatcher = dispatcherProvider,
                apiCall = { apiService.fetchCharacterDetail(id) },
                apiResultOf = { character: CharacterDto -> Result.success(character.asExternalUiModel()) },
            )
    }
