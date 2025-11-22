package com.droidos.data.repository.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.common.utils.Constants.INITIAL_PAGE
import com.droidos.data.remote.CharactersService
import com.droidos.model.beans.CharacterDto
import retrofit2.HttpException
import java.io.IOException

class SearchCharacterDataSource(
    private val apiService: CharactersService,
    private val name: String,
) : PagingSource<Int, CharacterDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val page = params.key ?: INITIAL_PAGE

        return try {
            handleSuccessfulLoad(page)
        } catch (e: HttpException) {
            handleHttpException(e, page)
        } catch (e: IOException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }

    private suspend fun handleSuccessfulLoad(page: Int): LoadResult<Int, CharacterDto> {
        val response = apiService.searchCharacters(page = page, name = name)

        return if (response.results.isEmpty()) {
            createEmptyResultPage(page)
        } else {
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1,
            )
        }
    }

    private fun handleHttpException(
        e: HttpException,
        page: Int,
    ): LoadResult<Int, CharacterDto> =
        when (e.code()) {
            NOT_FOUND -> createEmptyResultPage(page)
            else -> LoadResult.Error(e)
        }

    private fun createEmptyResultPage(page: Int): LoadResult.Page<Int, CharacterDto> =
        LoadResult.Page(
            data = emptyList(),
            prevKey = if (page == INITIAL_PAGE) null else page - 1,
            nextKey = null,
            itemsBefore = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
            itemsAfter = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
        )

    companion object {
        const val NOT_FOUND = 404
    }
}
