package com.droidos.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.droidos.design.components.ErrorCard
import com.droidos.design.theme.RMCTheme
import com.droidos.home.actions.HomeActions
import com.droidos.home.composables.CharacterItem
import com.droidos.home.composables.CharacterShimmerItem
import com.droidos.home.composables.NoResultsPlaceholder
import com.droidos.home.composables.RenderNoCharacters
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.beans.CharacterUIModel
import retrofit2.HttpException


@Composable
fun HomesRoute(
    onNavToDetails: (Int) -> Unit,
    querySearch: String,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val characterState: LazyListState = rememberLazyListState()
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(querySearch) {
        viewModel.emitAction(HomeActions.OnQueryChange(querySearch))
    }

    HomeScreen(
        uiState = uiState,
        characters = characters,
        characterState = characterState,
        onNavToDetails = onNavToDetails
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    characters: LazyPagingItems<CharacterUIModel>,
    characterState: LazyListState,
    onNavToDetails: (Int) -> Unit,
) {
    val refreshState = characters.loadState.refresh
    val appendState = characters.loadState.append

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = characterState
    ) {
        when (refreshState) {
            is LoadState.Loading ->
                items(10) {
                    CharacterShimmerItem()
                }

            is LoadState.Error ->
                item {
                    RenderError(refreshState.error, uiState, characters)
                }

            else -> {
                if (characters.itemCount > 0) {
                    items(count = characters.itemCount) { index ->
                        characters[index]?.let { character ->
                            CharacterItem(
                                uiState = character,
                                onCLick = { onNavToDetails(character.id) }
                            )
                        }
                    }
                } else {
                    item {
                        RenderNoCharacters(
                            characters = characters,
                            uiState = uiState,
                            appendState = appendState
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RenderError(
    error: Throwable,
    uiState: HomeUiState,
    characters: LazyPagingItems<CharacterUIModel>
) {
    if (error is HttpException && error.code() == 404) {
        NoResultsPlaceholder(
            message = stringResource(
                R.string.no_characters_found_for,
                uiState.searchQuery
            )
        )
    } else {
        ErrorCard {
            characters.refresh()
        }
    }
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun CharacterItemPreview_Light() {
    RMCTheme {
        CharacterItem(
            uiState = CharacterUIModel(
                id = 1,
                name = "Morty Smith",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                species = "Alive",
                status = "Human"
            ),
            onCLick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun CharacterItemPreview_Dark() {
    RMCTheme {

        CharacterItem(
            uiState = CharacterUIModel(
                id = 1,
                name = "Morty Smith",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                species = "Alive",
                status = "Human"
            ),
            onCLick = {}
        )
    }
}
