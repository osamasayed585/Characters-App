package com.droidos.home

import androidx.paging.LoadState
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

/**
 * Covers the state table behind the Home list. The case that matters most is the first one:
 * zero items is not the same thing as "the API returned nothing".
 */
class HomeListContentTest {
    private val idle = LoadState.NotLoading(endOfPaginationReached = false)
    private val exhausted = LoadState.NotLoading(endOfPaginationReached = true)

    private fun httpError(code: Int): LoadState.Error =
        LoadState.Error(
            HttpException(
                Response.error<Any>(code, "".toResponseBody("application/json".toMediaType())),
            ),
        )

    @Test
    fun `refresh loading with zero items shows shimmer, not the empty placeholder`() {
        homeListContent(
            refreshState = LoadState.Loading,
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Loading
    }

    @Test
    fun `first frame before the pager starts shows shimmer`() {
        // LazyPagingItems seeds loadState with refresh = Loading and both others NotLoading
        // (incomplete) until the presenter reports anything. Nothing has loaded, so nothing
        // may claim the list is empty.
        homeListContent(
            refreshState = LoadState.Loading,
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Loading
    }

    @Test
    fun `settled refresh with zero items but pagination not exhausted shows shimmer`() {
        // The hole the empty placeholder used to fall through. No load has proven the source
        // empty, so this is still a loading state.
        homeListContent(
            refreshState = idle,
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Loading
    }

    @Test
    fun `settled refresh with zero items and pagination exhausted shows the empty placeholder`() {
        // Both data sources return nextKey = null for an empty page, which is what makes
        // append reach end of pagination. This is the genuine "no results" state.
        homeListContent(
            refreshState = idle,
            appendState = exhausted,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Empty
    }

    @Test
    fun `a 404 refresh shows the empty placeholder rather than an error`() {
        homeListContent(
            refreshState = httpError(404),
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Empty
    }

    @Test
    fun `a non-404 http error shows the error card`() {
        homeListContent(
            refreshState = httpError(500),
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Error
    }

    @Test
    fun `a non-http error shows the error card`() {
        homeListContent(
            refreshState = LoadState.Error(IllegalStateException("boom")),
            appendState = idle,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Error
    }

    @Test
    fun `loading takes precedence over an exhausted append from the previous generation`() {
        // Leaving an empty result behind: the new generation reports refresh = Loading while
        // the stale append state still says end of pagination. Shimmer must win.
        homeListContent(
            refreshState = LoadState.Loading,
            appendState = exhausted,
            itemCount = 0,
        ) shouldBeEqualTo HomeListContent.Loading
    }

    @Test
    fun `items are shown once any have loaded`() {
        homeListContent(
            refreshState = idle,
            appendState = idle,
            itemCount = 4,
        ) shouldBeEqualTo HomeListContent.Characters
    }

    @Test
    fun `items are shown while a further page is appending`() {
        homeListContent(
            refreshState = idle,
            appendState = LoadState.Loading,
            itemCount = 4,
        ) shouldBeEqualTo HomeListContent.Characters
    }

    @Test
    fun `items survive an exhausted append`() {
        homeListContent(
            refreshState = idle,
            appendState = exhausted,
            itemCount = 4,
        ) shouldBeEqualTo HomeListContent.Characters
    }
}
