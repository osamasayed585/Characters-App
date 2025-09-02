package com.yassir.home.event

import androidx.paging.PagingData
import com.yassir.common.base.ScreenEvent
import com.yassir.model.beans.CharacterUIModel


sealed class ArticleEvent: ScreenEvent {
    data class ShowArticles(val articles: PagingData<CharacterUIModel>) : ArticleEvent()
}