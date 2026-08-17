package com.droidos.data.repository.search

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.common.utils.Constants.PAGE_SIZE
import com.droidos.data.charactersResponse
import com.droidos.data.charactersResponseOf
import com.droidos.data.mockCharactersResponse
import com.droidos.data.remote.CharactersService
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeEmpty
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchCharacterDataSourceTest {
    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)
    private lateinit var sut: SearchCharacterDataSource
    private val apiService = mockk<CharactersService>()
    private val name = "OsamaSayed"

    @Before
    fun setUp() {
        sut = SearchCharacterDataSource(apiService, name)
    }

    @Test
    fun load() =
        runTest {
            // Given
            coEvery { apiService.searchCharacters(name = name) } returns mockCharactersResponse
            val params = PagingSource.LoadParams.Refresh(1, 10, false)

            // When
            val result = sut.load(params)

            // Than
            result shouldBeInstanceOf PagingSource.LoadResult.Page::class
            val pageResult = result as PagingSource.LoadResult.Page

            pageResult.data.shouldNotBeEmpty()
            pageResult.data shouldHaveSize 2
            pageResult.nextKey shouldBe null
            pageResult.prevKey shouldBe null
        }

    @Test
    fun `pagingSource emits no duplicate ids when pages overlap`() =
        runTest {
            // Given - a shifting result window repeats ids 4 and 5 on the second page
            coEvery { apiService.searchCharacters(page = 1, name = name) } returns
                charactersResponseOf(ids = listOf(1, 2, 3, 4, 5), next = NEXT_PAGE_URL)
            coEvery { apiService.searchCharacters(page = 2, name = name) } returns
                charactersResponseOf(ids = listOf(4, 5, 6, 7, 8), next = NEXT_PAGE_URL)

            // When
            val firstPage = sut.load(PagingSource.LoadParams.Refresh(1, PAGE_SIZE, false)) as PagingSource.LoadResult.Page
            val secondPage = sut.load(PagingSource.LoadParams.Append(2, PAGE_SIZE, false)) as PagingSource.LoadResult.Page

            // Then
            val emittedIds = (firstPage.data + secondPage.data).map { it.id }
            emittedIds shouldBeEqualTo emittedIds.distinct()
            emittedIds shouldBeEqualTo listOf(1, 2, 3, 4, 5, 6, 7, 8)
            secondPage.nextKey shouldBeEqualTo 3
        }

    @Test
    fun getRefreshKey() =
        runTest {
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

    @After
    fun tearDown() {
        clearAllMocks()
    }

    private companion object {
        const val NEXT_PAGE_URL = "https://rickandmortyapi.com/api/character?page=3"
    }
}
