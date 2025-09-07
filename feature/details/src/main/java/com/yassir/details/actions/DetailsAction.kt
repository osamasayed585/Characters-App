package com.yassir.details.actions

import com.yassir.common.base.ScreenAction
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorEntity

sealed class DetailsAction : ScreenAction {
    data class OnGetCharacterDetails(val character: CharacterUIModel) : DetailsAction()
    data class OnGetError(val type: ErrorEntity) : DetailsAction()
    data object ClearError : DetailsAction()
}
