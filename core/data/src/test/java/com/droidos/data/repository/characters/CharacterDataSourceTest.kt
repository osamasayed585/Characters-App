package com.droidos.data.repository.characters

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.data.TestDispatcherProvider
import com.droidos.data.charactersResponse
import com.droidos.data.mockResponse
import com.droidos.data.remote.CharactersService
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CharacterDataSourceTest {

    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)
    private lateinit var sut: CharacterDataSource
    private val dispatcherProvider = TestDispatcherProvider()
    private val apiService = mockk<CharactersService>()
    private val errorHandler = mockk<ErrorHandler>()

    @Before
    fun setUp() {
        sut = CharacterDataSource(apiService, errorHandler, dispatcherProvider)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun load() = runTest {
        // Given
        coEvery { apiService.fetchCharacters() } returns mockResponse
        val params = PagingSource.LoadParams.Refresh(1, 10, false)

        // When
        val result = sut.load(params)

        // Than
        result shouldBeInstanceOf PagingSource.LoadResult.Page::class
        val pageResult = result as PagingSource.LoadResult.Page
        pageResult.data.shouldNotBeEmpty()
        pageResult.data shouldHaveSize 2
        pageResult.nextKey shouldBeEqualTo 2
        pageResult.prevKey shouldBeEqualTo null
    }

    @Test
    fun getRefreshKey() {
        // Given
        val mockPages = listOf(
            PagingSource.LoadResult.Page(
                data = charactersResponse,
                prevKey = 1,
                nextKey = 3,
                itemsBefore = 0,
                itemsAfter = 0
            )
        )

        val pagingState = PagingState(
            pages = mockPages,
            anchorPosition = 15,
            config = PagingConfig(pageSize = 20),
            leadingPlaceholderCount = 0
        )

        // When
        val refreshKey = sut.getRefreshKey(pagingState)

        // Then
        refreshKey shouldBeEqualTo  2
    }
}