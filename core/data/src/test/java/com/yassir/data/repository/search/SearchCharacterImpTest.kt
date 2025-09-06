package com.yassir.data.repository.search

import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.yassir.common.di.DispatcherProvider
import com.yassir.data.charactersResponse
import com.yassir.data.mockCharactersResponse
import com.yassir.data.remote.CharactersService
import com.yassir.datastore.LocalDataStore
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test

class SearchCharacterImpTest {

    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)

    private lateinit var sut: SearchCharacterImp
    private val apiService: CharactersService = mockk()
    private val preferences: LocalDataStore = mockk()
    private val dispatcherProvider: DispatcherProvider = mockk()
    private val errorHandler: ErrorHandler = mockk()

    @Before
    fun setUp() {
        sut = SearchCharacterImp(
            apiService = apiService,
            preferences = preferences,
            dispatcherProvider = dispatcherProvider,
            errorHandler = errorHandler
        )
    }

    @Ignore(" Caused by -> java.lang.RuntimeException: Method isLoggable in android.util.Log not mocked. See https://developer.android.com/r/studio-ui/build/not-mocked for details.")
    @Test
    fun searchCharacter() = runTest {
        val characterName = "Rick"
        coEvery {
            apiService.searchCharacters(name = characterName)
        } returns mockCharactersResponse

        val result = sut.searchCharacters(characterName)

        result.test {
            awaitItem() shouldBeEqualTo charactersResponse
        }
    }


    @After
    fun tearDown() {
        clearAllMocks()
    }
}