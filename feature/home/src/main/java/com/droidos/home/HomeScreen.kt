package com.droidos.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import com.droidos.design.components.ErrorCard
import com.droidos.home.actions.HomeActions
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.CharacterModel

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
        onNavToDetails = onNavToDetails,
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    characters: LazyPagingItems<CharacterModel>,
    characterState: LazyListState,
    onNavToDetails: (Int) -> Unit,
) {
    val refreshState = characters.loadState.refresh
    val appendState = characters.loadState.append

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = characterState,
    ) {
        when (homeListContent(refreshState, appendState, characters.itemCount)) {
            HomeListContent.Loading -> shimmerItems()

            HomeListContent.Empty -> noResultsItem(uiState.searchQuery)

            HomeListContent.Error -> errorItem(onRetry = { characters.refresh() })

            HomeListContent.Characters -> {
                characterItems(characters, onNavToDetails)
                appendStateItems(appendState, onRetry = { characters.retry() })
            }
        }
    }
}

private fun LazyListScope.shimmerItems(count: Int = SHIMMER_ITEM_COUNT) {
    items(count) {
        CharacterShimmerItem()
    }
}

private fun LazyListScope.noResultsItem(searchQuery: String) {
    item {
        NoResultsPlaceholder(
            // `isBlank` matches the branch HomeViewModel uses to decide between the search and
            // the unfiltered use case, so the wording always agrees with what was requested.
            message =
                if (searchQuery.isBlank()) {
                    stringResource(R.string.no_characters_found)
                } else {
                    stringResource(R.string.no_characters_found_for, searchQuery)
                },
        )
    }
}

private fun LazyListScope.errorItem(onRetry: () -> Unit) {
    item {
        ErrorCard(onTapRetry = onRetry)
    }
}

private fun LazyListScope.characterItems(
    characters: LazyPagingItems<CharacterModel>,
    onNavToDetails: (Int) -> Unit,
) {
    items(
        count = characters.itemCount,
        key = characters.itemKey { it.id },
        contentType = characters.itemContentType(),
    ) { index ->
        characters[index]?.let { character ->
            CharacterItem(
                uiState = character,
                onClick = { onNavToDetails(character.id) },
            )
        }
    }
}

private fun LazyListScope.appendStateItems(
    appendState: LoadState,
    onRetry: () -> Unit,
) {
    when (appendState) {
        is LoadState.Loading -> shimmerItems()

        is LoadState.Error -> errorItem(onRetry)

        else -> Unit
    }
}

private const val SHIMMER_ITEM_COUNT = 10

@Composable
fun NoResultsPlaceholder(message: String) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
