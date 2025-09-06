package com.yassir.data.repository.search

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yassir.data.charactersResponse
import com.yassir.data.mockCharactersResponse
import com.yassir.data.remote.CharactersService
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
    fun load() = runTest {
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
    fun getRefreshKey() = runTest {
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


    @After
    fun tearDown() {
        clearAllMocks()
    }

}