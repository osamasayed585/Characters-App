package com.droidos.home.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.droidos.design.components.ErrorCard
import com.droidos.home.R
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.beans.CharacterUIModel

@Composable
 fun RenderNoCharacters(
    characters: LazyPagingItems<CharacterUIModel>,
    uiState: HomeUiState,
    appendState: LoadState,
    ) {
    if (characters.itemCount == 0) {
        NoResultsPlaceholder(
            message = stringResource(
                R.string.no_characters_found_for,
                uiState.searchQuery
            )
        )
    } else if (appendState is LoadState.Loading) {
        CharacterShimmerItem()

    } else if (appendState is LoadState.Error) {
        ErrorCard {
            characters.retry()
        }
    }
}