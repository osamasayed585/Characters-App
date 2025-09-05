package com.yassir.data.repository.characters

import androidx.paging.PagingData
import com.yassir.common.di.DispatcherProvider
import com.yassir.data.remote.CharactersService
import com.yassir.datastore.LocalDataStore
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetCharactersRepositoryImpTest {

    private val mockApiService: CharactersService = mockk()
    private val mockPreferences: LocalDataStore = mockk()
    private val mockDispatcherProvider: DispatcherProvider = mockk()
    private val errorHandler: ErrorHandler = mockk()

    @Test
    fun `Given a query, When requestCharacters is called, Then it should return a flow of PagingData`() =
        runTest {
            val repo = GetCharactersRepositoryImp(
                apiService = mockApiService,
                preferences = mockPreferences,
                dispatcherProvider = mockDispatcherProvider,
                errorHandler = errorHandler
            )

            val result: Flow<PagingData<CharacterUIModel>> = repo.requestCharacters()

    }

}