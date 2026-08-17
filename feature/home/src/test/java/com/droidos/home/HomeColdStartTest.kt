package com.droidos.home

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.domain.useCases.CharactersUseCases
import com.droidos.home.actions.HomeActions
import com.droidos.model.CharacterModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldNotContain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Drives the real [HomeViewModel] flow through a real Paging presenter and checks what
 * [homeListContent] would render at every point on the timeline.
 *
 * [Probe] mirrors what `LazyPagingItems` does: it holds a [PagingDataPresenter] and falls back
 * to `refresh = Loading` while the presenter has not reported any state yet. That fallback is
 * the reason a cold start is a loading state rather than an empty one, so it has to be part of
 * the simulation rather than assumed.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class HomeColdStartTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var useCases: CharactersUseCases

    @Before
    fun setup() {
        MockKAnnotations.init(this)
    }

    private class Probe : PagingDataPresenter<CharacterModel>(Dispatchers.Main) {
        override suspend fun presentPagingDataEvent(event: PagingDataEvent<CharacterModel>) = Unit

        fun render(): HomeListContent {
            val states: CombinedLoadStates? = loadStateFlow.value
            return homeListContent(
                refreshState = states?.refresh ?: LoadState.Loading,
                appendState = states?.append ?: LoadState.NotLoading(false),
                itemCount = snapshot().size,
            )
        }
    }

    private class SlowSource(
        private val page: List<CharacterModel>,
        private val latencyMs: Long,
    ) : PagingSource<Int, CharacterModel>() {
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterModel> {
            delay(latencyMs)
            // An empty page reports no next key, which is what drives append to end of
            // pagination and makes the genuine empty state distinguishable.
            return LoadResult.Page(data = page, prevKey = null, nextKey = null)
        }

        override fun getRefreshKey(state: PagingState<Int, CharacterModel>): Int? = null
    }

    private fun pagerOf(
        page: List<CharacterModel>,
        latencyMs: Long,
    ) = Pager(PagingConfig(pageSize = 20)) { SlowSource(page, latencyMs) }.flow

    @Test
    fun `cold start never renders the empty placeholder before the api responds`() =
        runTest {
            coEvery { useCases.getCharactersUseCase() } returns
                pagerOf(dummySuccess_HomeState, latencyMs = 800)

            val sut = HomeViewModel(useCases)
            val probe = Probe()
            val rendered = mutableListOf<HomeListContent>()

            val job = launch { sut.characters.collect { probe.collectFrom(it) } }

            // Covers the 500 ms debounce window, during which no Pager exists at all, plus
            // the 800 ms the API takes to answer.
            repeat(80) {
                advanceTimeBy(25)
                rendered += probe.render()
            }
            job.cancel()

            rendered shouldNotContain HomeListContent.Empty
            rendered.first() shouldBeEqualTo HomeListContent.Loading
            rendered.last() shouldBeEqualTo HomeListContent.Characters
            rendered shouldContain HomeListContent.Loading
        }

    @Test
    fun `an api that genuinely returns nothing does render the empty placeholder`() =
        runTest {
            coEvery { useCases.getCharactersUseCase() } returns
                pagerOf(emptyList(), latencyMs = 300)

            val sut = HomeViewModel(useCases)
            val probe = Probe()
            val rendered = mutableListOf<HomeListContent>()

            val job = launch { sut.characters.collect { probe.collectFrom(it) } }
            repeat(80) {
                advanceTimeBy(25)
                rendered += probe.render()
            }
            job.cancel()

            rendered.first() shouldBeEqualTo HomeListContent.Loading
            rendered.last() shouldBeEqualTo HomeListContent.Empty
        }

    @Test
    fun `changing the query never flashes the empty placeholder`() =
        runTest {
            coEvery { useCases.getCharactersUseCase() } returns
                pagerOf(dummySuccess_HomeState, latencyMs = 300)
            coEvery { useCases.getSearchUseCase(any()) } returns
                pagerOf(dummySuccess_HomeState.take(2), latencyMs = 800)

            val sut = HomeViewModel(useCases)
            val probe = Probe()

            val job = launch { sut.characters.collect { probe.collectFrom(it) } }
            repeat(60) { advanceTimeBy(25) }

            val rendered = mutableListOf<HomeListContent>()
            sut.emitAction(HomeActions.OnQueryChange("Rick"))
            repeat(80) {
                advanceTimeBy(25)
                rendered += probe.render()
            }
            job.cancel()

            rendered shouldNotContain HomeListContent.Empty
            rendered.last() shouldBeEqualTo HomeListContent.Characters
        }
}
