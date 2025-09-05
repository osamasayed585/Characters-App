package com.yassir.details

import com.yassir.details.state.DetailsUiState
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorEntity

const val ERROR_MESSAGE = "Error message"


val defaultDetailCharacterUi = CharacterUIModel(
    id = 9441, name = "John Moreno", status = "regione", species = "ceteros", image = "regione"

)

val dummySuccess_DetailsState = DetailsUiState(
    errorEntity = null,
    name = defaultDetailCharacterUi.name,
    image = defaultDetailCharacterUi.image,
    status = defaultDetailCharacterUi.status,
    species = defaultDetailCharacterUi.species,
)


val dummySuccess_SpecificTitleDetail = DetailsUiState(
    errorEntity = null,
    name = defaultDetailCharacterUi.name,
    image = defaultDetailCharacterUi.image,
    status = defaultDetailCharacterUi.status,
    species = defaultDetailCharacterUi.species,
)

val dummyError_DetailsState = DetailsUiState(
    errorEntity = ErrorEntity.Unknown(),
    name = defaultDetailCharacterUi.name,
    image = defaultDetailCharacterUi.image,
    status = defaultDetailCharacterUi.status,
    species = defaultDetailCharacterUi.species,
)
