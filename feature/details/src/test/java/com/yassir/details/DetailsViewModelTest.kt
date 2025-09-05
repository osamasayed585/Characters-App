package com.yassir.details

import com.yassir.common.di.DispatcherProvider
import com.yassir.domain.useCases.GetCharacterDetailsUseCase
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class DetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var useCase: GetCharacterDetailsUseCase
    private lateinit var dispatcher: DispatcherProvider

    @Before
    fun setUp() {
        val fakeDetailsUseCase = FakeCharacterDetailsRepositoryImp()
        useCase = GetCharacterDetailsUseCase(fakeDetailsUseCase)
        dispatcher = TestDispatcherProvider(StandardTestDispatcher())
    }

    @Test
    fun `requestCharacterDetails with all characters returns success and updates uiState`() = runTest {
        val viewModel = DetailsViewModel(useCase, dispatcher)

        viewModel.requestCharacterDetails(1)

        assertEquals(dummySuccess_DetailsState, viewModel.uiState.value)
    }

    @Test
    fun `requestCharacterDetails with specific title returns success and updates uiState`() = runTest {
        val viewModel = DetailsViewModel(useCase, dispatcher)

        viewModel.requestCharacterDetails(1)

        assertEquals(dummySuccess_SpecificTitleDetail, viewModel.uiState.value)
    }

    @Test
    fun `requestCharacterDetails with empty title returns error and updates uiState`() = runTest {
        val viewModel = DetailsViewModel(useCase, dispatcher)

        viewModel.requestCharacterDetails(1)

        assertEquals(dummyError_DetailsState, viewModel.uiState.value)
    }
}


