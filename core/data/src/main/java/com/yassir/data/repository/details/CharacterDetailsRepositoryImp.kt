package com.yassir.data.repository.details

import com.yassir.common.di.DispatcherProvider
import com.yassir.data.mapper.asExternalUiModel
import com.yassir.data.remote.CharactersService
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.model.beans.CharacterDto
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import com.yassir.network.di.errorHandler.safeApiCall
import javax.inject.Inject

class CharacterDetailsRepositoryImp @Inject constructor(
    private val apiService: CharactersService,
    private val preferences: LocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandler: ErrorHandler
) : CharacterDetailsRepository {

    override suspend fun requestCharacterDetails(id: Int) = safeApiCall(
        errorHandler = errorHandler,
        dispatcher = dispatcherProvider,
        apiCall = { apiService.fetchCharacterDetail(id) },
        apiResultOf = { character: CharacterDto -> Result.success(character.asExternalUiModel()) }
    )
}
