package com.droidos.home

import androidx.paging.LoadState
import retrofit2.HttpException

/** What the list shows for one combination of Paging states. */
internal enum class HomeListContent {
    Loading,
    Empty,
    Error,
    Characters,
}

/**
 * Maps Paging's state onto what the list renders; tested in HomeListContentTest.
 * Branch order is load-bearing — do not collapse it.
 *
 * The cold-start "No characters found" flash was never root-caused: reported on a build
 * predating this file, logcat never captured. Ruled out — branch ordering; PagingData.empty()
 * (null load states, so LazyPagingItems holds refresh = Loading); the OkHttp cache (cold on
 * fresh install). Empty therefore needs proof a load finished AND exhausted the source.
 *
 * `itemCount == 0 -> Loading` is the catch-all. TRAP — it assumes an empty page ends
 * pagination. Both data sources return nextKey = null when empty; one returning a non-null
 * nextKey with an empty page would shimmer forever, with no error.
 */
internal fun homeListContent(
    refreshState: LoadState,
    appendState: LoadState,
    itemCount: Int,
): HomeListContent =
    when {
        refreshState is LoadState.Loading -> HomeListContent.Loading

        refreshState.isNotFound() -> HomeListContent.Empty

        refreshState is LoadState.Error -> HomeListContent.Error

        refreshState is LoadState.NotLoading &&
            appendState.endOfPaginationReached &&
            itemCount == 0 -> HomeListContent.Empty

        itemCount == 0 -> HomeListContent.Loading

        else -> HomeListContent.Characters
    }

/** A 404 from the search endpoint means "no matches", not a failure worth an error card. */
private fun LoadState.isNotFound(): Boolean {
    val error = (this as? LoadState.Error)?.error
    return error is HttpException && error.code() == 404
}
