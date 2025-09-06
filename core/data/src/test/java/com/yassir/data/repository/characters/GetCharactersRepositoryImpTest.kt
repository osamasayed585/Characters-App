package com.yassir.data.repository.characters

import app.cash.turbine.test
import com.yassir.data.TestDispatcherProvider
import com.yassir.data.mockCharactersResponse
import com.yassir.data.mockResponse
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

class GetCharactersRepositoryImpTest {

    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)
    private lateinit var sut: GetCharactersRepositoryImp
    private val dispatcherProvider = TestDispatcherProvider()
    private val apiService = mockk<CharactersService>()
    private val preferences = mockk<LocalDataStore>()
    private val errorHandler = mockk<ErrorHandler>()

    @Before
    fun setUp() {
        sut = GetCharactersRepositoryImp(apiService, preferences, dispatcherProvider, errorHandler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }


    @Test
    fun `Given a query, When fetchCharacters is called, Then it should return a flow of PagingData`() =
        runTest {
            coEvery { apiService.fetchCharacters() } returns mockResponse


            val result = sut.fetchCharacters()
            result.test {
                awaitItem() shouldBe mockCharactersResponse
            }
        }

}