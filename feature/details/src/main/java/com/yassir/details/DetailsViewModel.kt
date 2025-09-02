package com.yassir.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.yassir.common.base.BaseViewModel
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.details.event.DetailsEvent
import com.yassir.details.state.DetailsUiState
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
        val title = savedStateHandle.get<String>("") ?: ""
        requestArticleDetails(title)
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

            is DetailsEvent.OnGetArticleDetails -> createNewState(
                oldState.copy(
                    isLoading = false,
                    title = sideEffect.character.name,
                    image = sideEffect.character.image,
                    description = sideEffect.character.status,
                    publishedAt = sideEffect.character.status,
                    sourceName = sideEffect.character.status,
                )
            )

            is DetailsEvent.OnGetError -> createNewState(
                oldState.copy(
                    isLoading = false,
                    errorEntity = sideEffect.type
                )
            )
        }
    }


    /**
     * Requests article details for the given title.
     *
     * Launches a coroutine on the main dispatcher to fetch article details using the `getArticleDetailsUseCase`.
     * Emits a `DetailsEvent.OnGetArticleDetails` event with the article details if successful,
     * or a `DetailsEvent.OnGetError` event with the error message if an error occurs.
     *
     * @param title The title of the article to request details for.
     */
    fun requestArticleDetails(title: String) = viewModelScope.launch(dispatcher.io) {
        getCharacterDetailsUseCase(title).fold(
            onSuccess = { article ->
                emitEvent(DetailsEvent.OnGetArticleDetails(article))
            },
            onFailure = { throwable ->
                val asErrorEntity = throwable.asErrorEntity()
                emitEvent(DetailsEvent.OnGetError(asErrorEntity))
            }
        )
    }
}