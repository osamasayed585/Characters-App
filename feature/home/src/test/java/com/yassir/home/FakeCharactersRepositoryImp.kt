package com.yassir.home

import androidx.paging.PagingData
import com.yassir.common.utils.Constants
import com.yassir.domain.repository.CharactersRepository
import com.yassir.model.beans.ArticleUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCharactersRepositoryImp : CharactersRepository {

    override fun requestArticles(query: String): Flow<PagingData<ArticleUIModel>> {
        return when(query){
            Constants.ALL -> flowOf(PagingData.from(dummySuccess_HomeState))
            Constants.NEW_QUERY -> flowOf(PagingData.from(changes_dummySuccess_HomeState))
            Constants.REMOVE_QUERY -> flowOf(PagingData.from(changes_dummySuccess_HomeState))
            Constants.THROW_ERROR -> flowOf(PagingData.empty())
            else -> flowOf(PagingData.empty())

        }
    }
}