package com.droidos.home

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import com.droidos.design.theme.AppPreview
import com.droidos.design.theme.RMCTheme
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.CharacterModel
import com.droidos.model.testCharacters
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.IOException

/**
 * Previews for [HomeScreen]. They live beside the screen rather than inside it because
 * HomeScreen.kt was deliberately kept short; Android Studio picks previews up from anywhere
 * in the module.
 *
 * There is no `isLoading`/`error` field on [HomeUiState] — every renderable state of this
 * screen is decided by `homeListContent` from Paging's load states, so each preview drives
 * those directly via `PagingData.from(data, sourceLoadStates)`. That is the same mechanism
 * the presenter uses at runtime, so these are real states, not mocked-out shapes.
 */
private val sampleCharacters = testCharacters.take(4)

private fun loadStates(
    refresh: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
    append: LoadState = LoadState.NotLoading(endOfPaginationReached = false),
) = LoadStates(
    refresh = refresh,
    prepend = LoadState.NotLoading(endOfPaginationReached = true),
    append = append,
)

/** Wires fake [PagingData] through the real `collectAsLazyPagingItems` path. */
@Composable
private fun HomeScreenPreviewHost(
    pagingData: PagingData<CharacterModel>,
    uiState: HomeUiState = HomeUiState(),
) {
    RMCTheme {
        HomeScreen(
            uiState = uiState,
            characters = MutableStateFlow(pagingData).collectAsLazyPagingItems(),
            characterState = rememberLazyListState(),
            onNavToDetails = {},
        )
    }
}

/** The main case: a loaded page of characters. */
@AppPreview
@Composable
private fun HomeScreenPreview_Populated() {
    HomeScreenPreviewHost(
        PagingData.from(sampleCharacters, loadStates()),
    )
}

/** Refresh in flight — `HomeListContent.Loading`, the shimmer rows. */
@AppPreview
@Composable
private fun HomeScreenPreview_Loading() {
    HomeScreenPreviewHost(
        PagingData.from(emptyList(), loadStates(refresh = LoadState.Loading)),
    )
}

/**
 * Refresh settled, pagination exhausted, nothing to show, and no query typed.
 * Exercises the blank-query wording in `noResultsItem`.
 */
@AppPreview
@Composable
private fun HomeScreenPreview_Empty() {
    HomeScreenPreviewHost(
        PagingData.from(
            emptyList(),
            loadStates(append = LoadState.NotLoading(endOfPaginationReached = true)),
        ),
    )
}

/** Same empty state, but with a search active — the parameterised wording. */
@AppPreview
@Composable
private fun HomeScreenPreview_EmptySearch() {
    HomeScreenPreviewHost(
        pagingData =
            PagingData.from(
                emptyList(),
                loadStates(append = LoadState.NotLoading(endOfPaginationReached = true)),
            ),
        uiState = HomeUiState(searchQuery = "Birdperson"),
    )
}

/** A non-404 refresh failure — `HomeListContent.Error`, the retry card. */
@AppPreview
@Composable
private fun HomeScreenPreview_Error() {
    HomeScreenPreviewHost(
        PagingData.from(
            emptyList(),
            loadStates(refresh = LoadState.Error(IOException("Unable to reach the server"))),
        ),
    )
}

/** Content plus a page appending below it. */
@AppPreview
@Composable
private fun HomeScreenPreview_Appending() {
    HomeScreenPreviewHost(
        PagingData.from(sampleCharacters, loadStates(append = LoadState.Loading)),
    )
}

/** Content plus a failed append — the only way to eyeball the retry card mid-list. */
@AppPreview
@Composable
private fun HomeScreenPreview_AppendError() {
    HomeScreenPreviewHost(
        PagingData.from(
            sampleCharacters,
            loadStates(append = LoadState.Error(IOException("Unable to load more"))),
        ),
    )
}
