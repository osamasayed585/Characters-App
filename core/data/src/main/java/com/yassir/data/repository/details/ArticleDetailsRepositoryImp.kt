package com.yassir.data.repository.details

import com.yassir.common.di.DispatcherProvider
import com.yassir.data.mapper.asExternalUiModel
import com.yassir.data.remote.ArticlesService
import com.yassir.datastore.LocalDataStore
import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.model.beans.CharacterDto
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import com.yassir.network.di.errorHandler.safeApiCall
import javax.inject.Inject

class ArticleDetailsRepositoryImp @Inject constructor(
    private val apiService: ArticlesService,
    private val preferences: LocalDataStore,
    private val dispatcherProvider: DispatcherProvider,
    private val errorHandler: ErrorHandler
) : CharacterDetailsRepository {

    override suspend fun requestCharacterDetails(query: String) = safeApiCall(
        errorHandler = errorHandler,
        dispatcher = dispatcherProvider,
        apiCall = {
            apiService.requestCharacterDetail(query = query)
        },
        apiResultOf = { character: CharacterDto ->
            Result.success(character.asExternalUiModel())
        }
    )
}
