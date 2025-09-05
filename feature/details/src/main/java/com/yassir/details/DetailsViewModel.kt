package com.yassir.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yassir.common.base.BaseViewModel
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.details.event.DetailsEvent
import com.yassir.details.state.DetailsUiState
import com.yassir.details.state.DetailsUiState.DetailApiState
import com.yassir.domain.useCases.GetCharacterDetailsUseCase
import com.yassir.network.di.errorHandler.entities.asErrorEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
    private val dispatcher: DispatcherProvider,
    savedStateHandle: SavedStateHandle = SavedStateHandle(),
) : BaseViewModel<DetailsUiState, DetailsEvent>(DetailsUiState()) {

    init {
        createNewState(
            uiState.value.copy(
                id = savedStateHandle.get<Int>(Constants.ID) ?: -1
            )
        )
        requestCharacterDetails(uiState.value.id)
    }

    /**
     * Handles side effects emitted by the ViewModel and updates the UI state accordingly.
     *
     * @param oldState The current UI state.
     * @param sideEffect The side effect to handle.
     */
    override fun reduce(oldState: DetailsUiState, sideEffect: DetailsEvent) {
        when (sideEffect) {
            is DetailsEvent.ClearError -> createNewState(oldState.copy(errorEntity = null))

            is DetailsEvent.OnGetCharacterDetails -> createNewState(
                oldState.copy(
                    apiState = DetailApiState.Success,
                    name = sideEffect.character.name,
                    image = sideEffect.character.image,
                    status = sideEffect.character.status,
                    species = sideEffect.character.species,
                )
            )

            is DetailsEvent.OnGetError -> createNewState(
                oldState.copy(
                    apiState = DetailApiState.Error(sideEffect.type),
                    errorEntity = sideEffect.type
                )
            )
        }
    }



    /**
     * Requests character details for the given ID.
     *
     * Launches a coroutine on the IO dispatcher to fetch character details using the `getCharacterDetailsUseCase`.
     * Emits a `DetailsEvent.OnGetCharacterDetails` event with the character details if successful,
     * or a `DetailsEvent.OnGetError` event with the error message if an error occurs.
     *
     * @param id The ID of the character to request details for.
     */
    fun requestCharacterDetails(id: Int) = viewModelScope.launch(dispatcher.io) {
        getCharacterDetailsUseCase(id).fold(
            onSuccess = { character ->
                emitEvent(DetailsEvent.OnGetCharacterDetails(character))
            },
            onFailure = { throwable ->
                val asErrorEntity = throwable.asErrorEntity()
                emitEvent(DetailsEvent.OnGetError(asErrorEntity))
            }
        )
    }
}