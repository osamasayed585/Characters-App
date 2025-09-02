package com.yassir.details.event

import com.yassir.common.base.ScreenEvent
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorEntity

sealed class DetailsEvent : ScreenEvent {
    data class OnGetArticleDetails(val character: CharacterUIModel) : DetailsEvent()
    data class OnGetError(val type: ErrorEntity) : DetailsEvent()
    data object ClearError : DetailsEvent()
}
