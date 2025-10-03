package com.droidos.home

import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.droidos.domain.useCases.CharactersUseCases
import com.droidos.home.actions.HomeActions
import com.droidos.model.beans.testCharacters
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var useCases: CharactersUseCases
    private lateinit var sut: HomeViewModel


    @Before
    fun setup() {
        MockKAnnotations.init(this)
        coEvery { useCases.getCharactersUseCase() } returns flowOf(
            PagingData.from(dummySuccess_HomeState)
        )
        sut = HomeViewModel(useCases)
    }

    @After
    fun teardown() {
        clearAllMocks()
    }


    @Test
    fun `getCharacters with non blank query`() = runTest {
        // Test if getCharacters returns search results when the query is not blank.
        val query = "Rick Sanchez"
        coEvery { useCases.getSearchUseCase(query) } returns flowOf(PagingData.from(testCharacters))
        sut.emitAction(HomeActions.OnQueryChange(query))

        sut.characters.test {
            val result = awaitItem().collectData()
            Assert.assertTrue(result.isEmpty())
        }
    }

    @Test
    fun `getCharacters debounce functionality`() {
        // Test if getCharacters debounces rapid query changes and only fetches data for the latest query.
        // TODO implement test
    }

    @Test
    fun `getCharacters distinctUntilChanged functionality`() {
        // Test if getCharacters only fetches data when the query actually changes, not for identical consecutive queries.
        // TODO implement test
    }

    @Test
    fun `getCharacters error handling from getCharactersUseCase`() {
        // Test if getCharacters handles errors gracefully when getCharactersUseCase throws an exception.
        // TODO implement test
    }

    @Test
    fun `getCharacters error handling from getSearchUseCase`() {
        // Test if getCharacters handles errors gracefully when getSearchUseCase throws an exception.
        // TODO implement test
    }

    @Test
    fun `getCharacters empty PagingData from getCharactersUseCase`() {
        // Test if getCharacters handles an empty PagingData returned by getCharactersUseCase correctly.
        // TODO implement test
    }

    @Test
    fun `getCharacters empty PagingData from getSearchUseCase`() {
        // Test if getCharacters handles an empty PagingData returned by getSearchUseCase correctly.
        // TODO implement test
    }

    @Test
    fun `getCharacters with special characters in query`() {
        // Test how getCharacters handles queries containing special characters (e.g., symbols, emojis).
        // TODO implement test
    }

    @Test
    fun `getCharacters with very long query`() {
        // Test the behavior of getCharacters with an extremely long search query.
        // TODO implement test
    }

    @Test
    fun `getCharacters multiple subscribers`() {
        // Test if getCharacters behaves correctly when multiple subscribers collect the flow, leveraging cachedIn.
        // TODO implement test
    }

    @Test
    fun `getCharacters initial value`() {
        // Test if getCharacters emits PagingData.empty() as its initial value before any query is processed.
        // TODO implement test
    }

    @Test
    fun `getCharacters flow cancellation and restart`() {
        // Test if the flow correctly cancels and restarts when the viewModelScope is cancelled and restarted (if applicable in the test environment).
        // TODO implement test
    }

    @Test
    fun `getCharacters SharingStarted WhileSubscribed behavior`() {
        // Test that the upstream flow from use cases is active only when there's at least one subscriber and stops after 5000ms of no subscribers.
        // TODO implement test
    }

    @Test
    fun `reduce OnQueryChange updates UI state`() {
        // Test if reduce correctly updates the HomeUiState's searchQuery when an OnQueryChange event is received.
        // TODO implement test
    }

    @Test
    fun `reduce OnQueryChange with empty query string`() {
        // Test if reduce correctly updates the HomeUiState and searchQuery MutableStateFlow when OnQueryChange event has an empty query string.
        // TODO implement test
    }

    @Test
    fun `reduce OnQueryChange with special characters in query string`() {
        // Test if reduce correctly updates the HomeUiState and searchQuery MutableStateFlow when OnQueryChange event has a query string with special characters.
        // TODO implement test
    }

    @Test
    fun `reduce OnQueryChange with long query string`() {
        // Test if reduce correctly updates the HomeUiState and searchQuery MutableStateFlow when OnQueryChange event has a very long query string.
        // TODO implement test
    }

    @Test
    fun `reduce multiple OnQueryChange events`() {
        // Test if reduce correctly handles a sequence of OnQueryChange events, ensuring the state reflects the latest query.
        // TODO implement test
    }
}


private fun <T : Any> PagingData<T>.collectData(): List<T> = runBlocking {
    val items = mutableListOf<T>()
    flowOf(this@collectData).collect { pagingData ->
        pagingData.map { item ->
            items.add(item)
            println("this is a new item $item")
        }
    }
    if (items.isEmpty()) println("there's no items")
    else println("Here's our items")
    items
}
