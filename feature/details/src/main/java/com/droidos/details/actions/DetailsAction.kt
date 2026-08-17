package com.droidos.details.actions

import com.droidos.common.base.ScreenAction
import com.droidos.model.CharacterModel
import com.droidos.network.di.errorHandler.entities.ErrorEntity

sealed class DetailsAction : ScreenAction {
    data class OnGetCharacterDetails(
        val character: CharacterModel,
    ) : DetailsAction()

    data class OnGetError(
        val type: ErrorEntity,
    ) : DetailsAction()

    data object ClearError : DetailsAction()
}
