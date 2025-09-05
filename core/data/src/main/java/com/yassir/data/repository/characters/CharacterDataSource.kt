package com.yassir.data.repository.characters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yassir.common.di.DispatcherProvider
import com.yassir.common.utils.Constants.INITIAL_PAGE
import com.yassir.data.remote.CharactersService
import com.yassir.model.beans.CharacterDto
import com.yassir.model.response.CharactersResponse
import com.yassir.network.di.errorHandler.entities.ErrorHandler
import com.yassir.network.di.errorHandler.safeApiCall

class CharacterDataSource(
    private val apiService: CharactersService,
    private val errorHandler: ErrorHandler,
    private val dispatcherProvider: DispatcherProvider,
) : PagingSource<Int, CharacterDto>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val page = params.key ?: INITIAL_PAGE
        return safeApiCall(
            errorHandler = errorHandler,
            dispatcher = dispatcherProvider,
            apiCall = { apiService.fetchCharacters(page = page) },
            apiResultOf = { response -> Result.success(response) }
        ).fold(
            onSuccess = { characters: CharactersResponse ->
                LoadResult.Page(
                    data = characters.results,
                    prevKey = if (page == INITIAL_PAGE) null else page.minus(1),
                    nextKey = if (characters.results.isEmpty()) null else page.plus(1),
                )
            },
            onFailure = { it: Throwable ->
                it.printStackTrace()
                LoadResult.Error(Exception(it))
            }
        )
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}