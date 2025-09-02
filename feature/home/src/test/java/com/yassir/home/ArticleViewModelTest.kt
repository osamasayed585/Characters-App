package com.yassir.home

import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.yassir.common.di.DefaultDispatcherProvider
import com.yassir.common.utils.Constants
import com.yassir.domain.useCases.GetCharacterUseCase
import com.yassir.model.beans.ArticleUIModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ArticleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: ArticleViewModel


    @Before
    fun setup() {
        viewModel = createViewModel()
    }


    @Test
    fun `Given empty query When request articles Then emit empty data`() = runTest {
        viewModel.requestArticles(Constants.EMPTY_QUERY)

        viewModel.articles.test {
            val emittedList = awaitItem().collectData()
            val expectedList = PagingData.empty<ArticleUIModel>().collectData()

            expectedList shouldBeEqualTo emittedList
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given valid query When request articles Then emit valid data`() = runTest {
        viewModel.requestArticles(Constants.ALL)

        viewModel.articles.test {
            val emittedList = awaitItem().collectData()
            val expectedList = PagingData.from(dummySuccess_HomeState).collectData()

            expectedList shouldBeEqualTo emittedList
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given valid query When request articles and data changes Then emit changed data`() =
        runTest {
            viewModel.requestArticles(Constants.ALL)

            viewModel.articles.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestArticles(Constants.NEW_QUERY)

                val expectedListAfterChanges =
                    PagingData.from(changes_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedListAfterChanges

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given valid query When request articles and data removed Then emit removed data`() =
        runTest {
            viewModel.requestArticles(Constants.ALL)

            viewModel.articles.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestArticles(Constants.REMOVE_QUERY)

                val expectedAfterRemoval =
                    PagingData.from(remove_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedAfterRemoval

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given valid query When request articles and data inserted Then emit inserted data`() =
        runTest {
            viewModel.requestArticles(Constants.ALL)

            viewModel.articles.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestArticles(Constants.INSERT_QUERY)

                val expectedListAfterInsertion =
                    PagingData.from(insert_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedListAfterInsertion

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given throw error When request articles Then emit empty data`() = runTest {
        viewModel.requestArticles(Constants.THROW_ERROR)

        viewModel.articles.test {
            val expectedList = PagingData.empty<ArticleUIModel>().collectData()
            awaitItem().collectData() shouldBeEqualTo expectedList
            cancelAndIgnoreRemainingEvents()
        }
    }
}

private fun createViewModel(): ArticleViewModel {
    val dispatcherProvider = DefaultDispatcherProvider()
    val repo = FakeCharactersRepositoryImp()
    val useCase = GetCharacterUseCase(repo)
    return ArticleViewModel(useCase, dispatcherProvider)
}

private fun <T : Any> PagingData<T>.collectData(): List<T> = runBlocking {
    val items = mutableListOf<T>()
    flowOf(this@collectData).collect { pagingData ->
        pagingData.map { item ->
            items.add(item)
        }
    }
    items
}
