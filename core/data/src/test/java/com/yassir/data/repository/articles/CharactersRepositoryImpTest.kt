package com.yassir.data.repository.articles

import androidx.paging.PagingData
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.data.remote.ArticlesService
import com.yassir.datastore.LocalDataStore
import com.yassir.model.beans.ArticleUIModel
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class CharactersRepositoryImpTest {

    private val mockApiService: ArticlesService = mockk()
    private val mockPreferences: LocalDataStore = mockk()
    private val mockDispatcherProvider: DispatcherProvider = mockk()

    @Test
    fun `Given a query, When requestArticles is called, Then it should return a flow of PagingData`() = runTest {

        val repo = CharactersRepositoryImp(
            apiService = mockApiService,
            preferences = mockPreferences,
            dispatcherProvider = mockDispatcherProvider
        )

        val result: Flow<PagingData<ArticleUIModel>> = repo.requestArticles(Constants.ALL)

    }

}