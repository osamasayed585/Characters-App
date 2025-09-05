package com.yassir.home

import androidx.paging.PagingData
import androidx.paging.map
import app.cash.turbine.test
import com.yassir.common.di.DefaultDispatcherProvider
import com.yassir.domain.useCases.CharactersUseCases
import com.yassir.domain.useCases.GetCharacterUseCase
import com.yassir.model.beans.CharacterUIModel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.junit.Before
import org.junit.Rule
import org.junit.Test



class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var fakeRepository: FakeGetCharactersRepositoryImp
    private lateinit var useCases: CharactersUseCases
    private lateinit var viewModel: HomeViewModel



    @Before
    fun setup() {
        fakeRepository = FakeGetCharactersRepositoryImp()
        useCases = GetCharacterUseCase(fakeRepository)
        viewModel = HomeViewModel(useCases, DefaultDispatcherProvider())
    }


    @Test
    fun `Given empty When request characters Then emit empty data`() = runTest {
        fakeRepository.setCharacters(listOf())

        viewModel.requestCharacters()

        viewModel.characters.test {
            val emittedList = awaitItem().collectData()
            val expectedList = PagingData.empty<CharacterUIModel>().collectData()

            expectedList shouldBeEqualTo emittedList
        }
    }

    @Test
    fun `Given valid query When request characters Then emit valid data`() = runTest {
        viewModel.requestCharacters()

        viewModel.characters.test {
            val emittedList = awaitItem().collectData()
            val expectedList = PagingData.from(dummySuccess_HomeState).collectData()

            expectedList shouldBeEqualTo emittedList
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given valid query When request characters and data changes Then emit changed data`() =
        runTest {
            viewModel.requestCharacters()

            viewModel.characters.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestCharacters()

                val expectedListAfterChanges =
                    PagingData.from(changes_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedListAfterChanges

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given valid query When request characters and data removed Then emit removed data`() =
        runTest {
            viewModel.requestCharacters()

            viewModel.characters.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestCharacters()

                val expectedAfterRemoval =
                    PagingData.from(remove_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedAfterRemoval

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given valid query When request characters and data inserted Then emit inserted data`() =
        runTest {
            viewModel.requestCharacters()

            viewModel.characters.test {
                val expectedList = PagingData.from(dummySuccess_HomeState).collectData()
                awaitItem().collectData() shouldBeEqualTo expectedList

                viewModel.requestCharacters()

                val expectedListAfterInsertion =
                    PagingData.from(insert_dummySuccess_HomeState).collectData()

                awaitItem().collectData() shouldBeEqualTo expectedListAfterInsertion

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given throw error When request characters Then emit empty data`() = runTest {
        fakeRepository.setShouldThrowError(true)

        viewModel.requestCharacters()

        viewModel.characters.test {
            val expectedList = PagingData.empty<CharacterUIModel>().collectData()
            awaitItem().collectData() shouldBeEqualTo expectedList
            cancelAndIgnoreRemainingEvents()
        }
    }
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
