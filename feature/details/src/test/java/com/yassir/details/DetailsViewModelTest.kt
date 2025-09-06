package com.yassir.details

import app.cash.turbine.test
import com.yassir.common.di.DispatcherProvider
import com.yassir.details.state.DetailsUiState.DetailApiState
import com.yassir.domain.useCases.GetCharacterDetailsUseCase
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.junit4.MockKRule
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule(order = 0)
    val mockkRule = MockKRule(this)

    private lateinit var sut: DetailsViewModel
    private val useCase = mockk<GetCharacterDetailsUseCase>()
    private val dispatcher = TestDispatcherProvider()

    @Before
    fun setUp() {
        sut = DetailsViewModel(useCase, dispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `fetchCharacterDetails with characterId returns Loading uiState`() =
        runTest {
            // Given
            val characterId = 1
            coEvery { useCase.invoke(characterId) } returns Result.success(TestDetailCharacterUi)

            // When
            sut.fetchCharacterDetails(characterId)

            // Then
            sut.uiState.test {
                val init = awaitItem()
                init.apiState shouldBeEqualTo DetailApiState.Loading
            }
        }
}


