package com.droidos.data.repository.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.droidos.common.utils.Constants.INITIAL_PAGE
import com.droidos.data.remote.CharactersService
import com.droidos.model.beans.CharacterDto
import retrofit2.HttpException

class SearchCharacterDataSource(
    private val apiService: CharactersService,
    private val name: String,
) : PagingSource<Int, CharacterDto>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CharacterDto> {
        val page = params.key ?: INITIAL_PAGE

        return try {
            val response = apiService.searchCharacters(page = page, name = name)

            if (response.results.isEmpty()) {
                return LoadResult.Page(
                    data = emptyList(),
                    prevKey = if (page == INITIAL_PAGE) null else page - 1,
                    nextKey = null,
                    itemsBefore = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
                    itemsAfter = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
                )
            }

            LoadResult.Page(
                data = response.results,
                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                nextKey = if (response.info.next == null) null else page + 1,
            )
        } catch (e: Throwable) {
            when (e) {
                is HttpException -> {
                    when (e.code()) {
                        404 -> {
                            LoadResult.Page(
                                data = emptyList(),
                                prevKey = if (page == INITIAL_PAGE) null else page - 1,
                                nextKey = null,
                                itemsBefore = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
                                itemsAfter = if (page == INITIAL_PAGE) 0 else Int.MIN_VALUE,
                            )
                        }
                        else -> LoadResult.Error(e)
                    }
                }
                else -> LoadResult.Error(e)
            }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CharacterDto>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
}
