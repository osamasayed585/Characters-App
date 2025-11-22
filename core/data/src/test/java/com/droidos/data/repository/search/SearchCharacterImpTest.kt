package com.droidos.data.repository.search

import app.cash.turbine.test
import com.droidos.common.di.DispatcherProvider
import com.droidos.data.charactersResponse
import com.droidos.data.mockCharactersResponse
import com.droidos.data.remote.CharactersService
import com.droidos.datastore.LocalDataStore
import com.droidos.network.di.errorHandler.entities.ErrorHandler
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
        sut =
            SearchCharacterImp(
                apiService = apiService,
                preferences = preferences,
                dispatcherProvider = dispatcherProvider,
                errorHandler = errorHandler,
            )
    }

    @Ignore(
        " Caused by -> java.lang.RuntimeException: Method isLoggable in android.util.Log not mocked. See https://developer.android.com/r/studio-ui/build/not-mocked for details.",
    )
    @Test
    fun searchCharacter() =
        runTest {
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
