package com.droidos.data.repository.details

import com.droidos.data.TestDispatcherProvider
import com.droidos.data.character1
import com.droidos.data.errorResponse
import com.droidos.data.mockCharacterResponse
import com.droidos.data.remote.CharactersService
import com.droidos.datastore.LocalDataStore
import com.droidos.model.beans.CharacterDto
import com.droidos.network.di.errorHandler.entities.ErrorHandler
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response

class DetailsRepositoryImpTest {
    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)
    private lateinit var sut: DetailsRepositoryImp
    private val apiService = mockk<CharactersService>()
    private val dispatcherProvider = TestDispatcherProvider()
    private val preferences = mockk<LocalDataStore>()
    private val errorHandler = mockk<ErrorHandler>()

    @Before
    fun setUp() {
        sut = DetailsRepositoryImp(apiService, preferences, dispatcherProvider, errorHandler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun requestCharacterDetails_success() =
        runTest {
            // Given
            val characterId = 1
            coEvery { apiService.fetchCharacterDetail(characterId) } returns mockCharacterResponse

            // When
            val result: Response<CharacterDto> = apiService.fetchCharacterDetail(characterId)

            // Then
            result.isSuccessful shouldBe true
            result.body() shouldBeEqualTo character1
            result.body()!!.id shouldBeEqualTo characterId
            result.body()!!.name shouldBeEqualTo "Rick Sanchez"
        }

    @Test
    fun requestCharacterDetails_error() =
        runTest {
            // Given
            val characterId = 99
            coEvery { apiService.fetchCharacterDetail(characterId) } returns errorResponse

            // When
            val result: Response<CharacterDto> = apiService.fetchCharacterDetail(characterId)

            // Then
            result.isSuccessful shouldBe false
            result.code() shouldBeEqualTo 404
            result.body() shouldBe null
        }
}
