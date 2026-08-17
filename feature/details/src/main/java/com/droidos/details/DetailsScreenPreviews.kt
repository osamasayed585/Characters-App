package com.droidos.details

import androidx.compose.runtime.Composable
import com.droidos.design.theme.AppPreview
import com.droidos.design.theme.RMCTheme
import com.droidos.details.state.DetailsUiState
import com.droidos.network.di.errorHandler.entities.ErrorEntity

/**
 * Previews for [CharacterDetails]. They live beside the screen rather than inside it, to match
 * HomeScreenPreviews.kt; Android Studio picks previews up from anywhere in the module.
 *
 * The screen renders exactly one of [DetailsUiState.DetailApiState]'s three branches, so there
 * are three previews and no more. The `errorEntity` field on [DetailsUiState] is deliberately
 * not previewed here: it is consumed by `HandleError` in `CharacterDetailsRoute` as a snackbar,
 * not by this stateless screen.
 */
private val sampleCharacter =
    DetailsUiState(
        id = 1,
        name = "Rick Sanchez",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        species = "Human",
        status = "Alive",
        apiState = DetailsUiState.DetailApiState.Success,
    )

/** The main case: a fully loaded character. */
@AppPreview
@Composable
private fun CharacterDetailsPreview_Success() {
    RMCTheme {
        CharacterDetails(
            uiState = sampleCharacter,
            onRetry = {},
        )
    }
}

/** Details request in flight — the full-screen spinner. */
@AppPreview
@Composable
private fun CharacterDetailsPreview_Loading() {
    RMCTheme {
        CharacterDetails(
            uiState = DetailsUiState(apiState = DetailsUiState.DetailApiState.Loading),
            onRetry = {},
        )
    }
}

/**
 * Details request failed — the retry card. Any [ErrorEntity] renders identically, because
 * [CharacterDetails] passes no subtitle to `ErrorCard`, so one preview covers all ten.
 */
@AppPreview
@Composable
private fun CharacterDetailsPreview_Error() {
    RMCTheme {
        CharacterDetails(
            uiState =
                DetailsUiState(
                    apiState =
                        DetailsUiState.DetailApiState.Error(
                            ErrorEntity.Unknown("Failed to load character data."),
                        ),
                ),
            onRetry = {},
        )
    }
}
