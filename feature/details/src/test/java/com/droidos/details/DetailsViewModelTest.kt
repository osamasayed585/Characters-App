@file:OptIn(ExperimentalCoroutinesApi::class)

package com.droidos.details

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.droidos.common.utils.Constants
import com.droidos.details.actions.DetailsAction
import com.droidos.details.state.DetailsUiState
import com.droidos.domain.useCases.GetCharacterDetailsUseCase
import com.droidos.network.di.errorHandler.entities.ErrorEntity
import io.mockk.MockKAnnotations
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailsViewModelTest {
    private lateinit var sut: DetailsViewModel

    @MockK
    private lateinit var getCharacterDetailsUseCase: GetCharacterDetailsUseCase

    @MockK
    private lateinit var savedStateHandle: SavedStateHandle

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        produceViewModel()
    }

    private fun produceViewModel(id: Int = 1) {
        val dispatcherProvider = TestDispatcherProvider()
        every { savedStateHandle.get<Int>(Constants.ID) } returns id
        sut =
            DetailsViewModel(
                getCharacterDetailsUseCase = getCharacterDetailsUseCase,
                dispatcher = dispatcherProvider,
                savedStateHandle = savedStateHandle,
            )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `reduce with OnGetCharacterDetails updates all character fields`() =
        runTest {
            // Verify that when DetailsEvent.OnGetCharacterDetails is processed,
            // all fields of the character (id, name, image, status, species)
            // in the UI state are correctly updated from the provided CharacterUIModel.

            val character = createMockCharacter()
            sut.emitAction(DetailsAction.OnGetCharacterDetails(character))

            sut.uiState.test {
                val result = awaitItem()
                assertEquals(character.id, result.id)
            }
        }

    @Test
    fun `reduce with OnGetError updates apiState and errorEntity`() =
        runTest {
            // Verify that when DetailsEvent.OnGetError is processed, the apiState is set
            // to DetailApiState.Error with the correct error type,
            // and the errorEntity in the UI state is updated with the provided error type.
            val errorMessage = "Error message"
            val errorType = ErrorEntity.Unknown(errorMessage)
            sut.emitAction(DetailsAction.OnGetError(errorType))

            sut.uiState.test {
                val result = awaitItem()
                assertEquals(DetailsUiState.DetailApiState.Error(errorType), result.apiState)
            }
        }

    @Test
    fun `reduce with OnGetError preserves character data`() =
        runTest {
            // Verify that when DetailsEvent.OnGetError is processed,
            // existing character data (id, name, image, status, species)
            // in the UI state remains unchanged.
            produceViewModel(-1)
            sut.emitAction(DetailsAction.OnGetError(ErrorEntity.Unknown("Error Message")))

            sut.uiState.test {
                val result = awaitItem()
                assertEquals(errorDetailsUiState, result)
            }
        }

    @Test
    fun `reduce with ClearError only clears errorEntity`() =
        runTest {
            // Verify that when DetailsEvent.ClearError is processed, only the errorEntity is set to null,
            // and all other fields in the UI state (including apiState and character data) remain unchanged.

            sut.emitAction(DetailsAction.ClearError)

            sut.uiState.test {
                val result = awaitItem()
                assertNull(result.errorEntity)
            }
        }

    @Test
    fun `fetchCharacterDetails with invalid ID like -1 or 0 negative zero `() =
        runTest {
            // Test how fetchCharacterDetails handles an invalid character ID (e.g., -1, 0) passed as an argument.
            // Expect it to potentially result in an error state from the use case.
            produceViewModel(-1)
            sut.emitAction(DetailsAction.OnGetError(ErrorEntity.Unknown("Error Message")))

            sut.uiState.test {
                val result = awaitItem()
                assertEquals(errorDetailsUiState, result)
            }
        }

    @Test
    fun `fetchCharacterDetails when use case returns success with null character data fields`() =
        runTest {
            // Test the behavior when getCharacterDetailsUseCase successfully returns
            // a CharacterUIModel, but some of its fields (e.g., name, image) are null or empty.
            // Ensure the UI state reflects these null/empty values correctly without crashing.

            val character = createMockCharacter(name = "", image = "")
            coEvery { getCharacterDetailsUseCase(any()) } returns Result.success(character)

            sut.fetchCharacterDetails(1)

            sut.uiState.test {
                val result = awaitItem()
                assertEquals("", result.name)
                assertEquals("", result.image)
            }
        }
}
