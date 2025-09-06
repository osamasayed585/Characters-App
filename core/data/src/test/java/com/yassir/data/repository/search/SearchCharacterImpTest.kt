package com.yassir.data.repository.search

import app.cash.turbine.test
import com.yassir.common.di.DispatcherProvider
import com.yassir.data.mockCharactersResponse
import com.yassir.data.remote.CharactersService
import com.yassir.datastore.LocalDataStore
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.junit.After
import org.junit.Before
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

    @Test
    fun searchCharacter() = runTest {

        coEvery {
            apiService.searchCharacters(name = "Rick")
        } returns mockCharactersResponse

        val result = sut.searchCharacter("Rick")

        result.test {
            awaitItem() shouldBe mockCharactersResponse
        }
    }


    @After
    fun tearDown() {
        clearAllMocks()
    }
}