package com.droidos.data.repository.characters

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.common.utils.Constants.PAGE_SIZE
import com.droidos.data.TestDispatcherProvider
import com.droidos.data.charactersHttpResponseOf
import com.droidos.data.charactersResponse
import com.droidos.data.mockResponse
import com.droidos.data.remote.CharactersService
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEmpty
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
    fun load() =
        runTest {
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
    fun `pagingSource emits no duplicate ids when pages overlap`() =
        runTest {
            // Given - the remote window shifted between requests, so ids 4 and 5 land on
            // both pages. Page two still advertises a further page.
            coEvery { apiService.fetchCharacters(page = 1) } returns
                charactersHttpResponseOf(ids = listOf(1, 2, 3, 4, 5))
            coEvery { apiService.fetchCharacters(page = 2) } returns
                charactersHttpResponseOf(ids = listOf(4, 5, 6, 7, 8))

            // When - both pages are loaded through the same PagingSource generation
            val firstPage = sut.load(PagingSource.LoadParams.Refresh(1, PAGE_SIZE, false)) as PagingSource.LoadResult.Page
            val secondPage = sut.load(PagingSource.LoadParams.Append(2, PAGE_SIZE, false)) as PagingSource.LoadResult.Page

            // Then - every id reaching the UI is distinct
            val emittedIds = (firstPage.data + secondPage.data).map { it.id }
            emittedIds shouldBeEqualTo emittedIds.distinct()
            emittedIds shouldBeEqualTo listOf(1, 2, 3, 4, 5, 6, 7, 8)

            // And - the overlap is trimmed from the later page, not the earlier one
            firstPage.data.map { it.id } shouldBeEqualTo listOf(1, 2, 3, 4, 5)
            secondPage.data.map { it.id } shouldBeEqualTo listOf(6, 7, 8)

            // And - pagination still advances, since nextKey comes from the raw response
            secondPage.nextKey shouldBeEqualTo 3
        }

    @Test
    fun `pagingSource keeps paginating when a whole page is duplicates`() =
        runTest {
            // Given - page two repeats page one verbatim
            coEvery { apiService.fetchCharacters(page = 1) } returns
                charactersHttpResponseOf(ids = listOf(1, 2, 3))
            coEvery { apiService.fetchCharacters(page = 2) } returns
                charactersHttpResponseOf(ids = listOf(1, 2, 3))

            // When
            sut.load(PagingSource.LoadParams.Refresh(1, PAGE_SIZE, false))
            val secondPage = sut.load(PagingSource.LoadParams.Append(2, PAGE_SIZE, false)) as PagingSource.LoadResult.Page

            // Then - the page is emptied but the pager is not terminated
            secondPage.data.shouldBeEmpty()
            secondPage.nextKey shouldBeEqualTo 3
        }

    @Test
    fun `a fresh pagingSource does not inherit seen ids`() =
        runTest {
            // Given - the same first page served to two generations, as happens on refresh
            coEvery { apiService.fetchCharacters(page = 1) } returns
                charactersHttpResponseOf(ids = listOf(1, 2, 3))
            sut.load(PagingSource.LoadParams.Refresh(1, PAGE_SIZE, false))

            // When - Paging invalidates and the factory builds a new instance
            val refreshed = CharacterDataSource(apiService, errorHandler, dispatcherProvider)
            val result = refreshed.load(PagingSource.LoadParams.Refresh(1, PAGE_SIZE, false)) as PagingSource.LoadResult.Page

            // Then - the list rebuilds in full rather than deduplicating itself away
            result.data.map { it.id } shouldBeEqualTo listOf(1, 2, 3)
        }

    @Test
    fun getRefreshKey() {
        // Given
        val mockPages =
            listOf(
                PagingSource.LoadResult.Page(
                    data = charactersResponse,
                    prevKey = 1,
                    nextKey = 3,
                    itemsBefore = 0,
                    itemsAfter = 0,
                ),
            )

        val pagingState =
            PagingState(
                pages = mockPages,
                anchorPosition = 15,
                config = PagingConfig(pageSize = 20),
                leadingPlaceholderCount = 0,
            )

        // When
        val refreshKey = sut.getRefreshKey(pagingState)

        // Then
        refreshKey shouldBeEqualTo 2
    }
}
