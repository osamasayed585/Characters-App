package com.droidos.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.droidos.common.base.BaseViewModel
import com.droidos.common.di.DispatcherProvider
import com.droidos.common.utils.Constants
import com.droidos.details.actions.DetailsAction
import com.droidos.details.state.DetailsUiState
import com.droidos.details.state.DetailsUiState.DetailApiState
import com.droidos.domain.useCases.GetCharacterDetailsUseCase
import com.droidos.network.di.errorHandler.entities.asErrorEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
    @Inject
    constructor(
        private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
        private val dispatcher: DispatcherProvider,
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
    ) : BaseViewModel<DetailsUiState, DetailsAction>(DetailsUiState()) {
        init {
            updateState { copy(id = savedStateHandle.get<Int>(Constants.ID) ?: -1) }
            fetchCharacterDetails(state.id)
        }

        /**
         * Handles side effects emitted by the ViewModel and updates the UI state accordingly.
         *
         * @param oldState The current UI state.
         * @param action The side effect to handle.
         */
        override fun processAction(
            oldState: DetailsUiState,
            action: DetailsAction,
        ) {
            when (action) {
                is DetailsAction.ClearError -> createNewState(oldState.copy(errorEntity = null))

                is DetailsAction.OnGetCharacterDetails ->
                    createNewState(
                        oldState.copy(
                            apiState = DetailApiState.Success,
                            id = action.character.id,
                            name = action.character.name,
                            image = action.character.image,
                            status = action.character.status,
                            species = action.character.species,
                        ),
                    )

                is DetailsAction.OnGetError ->
                    createNewState(
                        oldState.copy(
                            apiState = DetailApiState.Error(action.type),
                            errorEntity = action.type,
                        ),
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
        fun fetchCharacterDetails(id: Int) =
            viewModelScope.launch(dispatcher.io) {
                getCharacterDetailsUseCase(id).fold(
                    onSuccess = { character ->
                        emitAction(DetailsAction.OnGetCharacterDetails(character))
                    },
                    onFailure = { throwable ->
                        val asErrorEntity = throwable.asErrorEntity()
                        emitAction(DetailsAction.OnGetError(asErrorEntity))
                    },
                )
            }
    }
