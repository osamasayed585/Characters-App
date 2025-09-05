package com.yassir.details

import com.yassir.details.state.DetailsUiState
import com.yassir.model.beans.CharacterUIModel

const val ERROR_MESSAGE = "Error message"
const val SPECIFIC_TITLE = "the specific title"


val defaultDetailCharacterUi = CharacterUIModel(
    id = 9441, name = "John Moreno", status = "regione", species = "ceteros", image = "regione"

)

val specificTitleCharacterUi = CharacterUIModel(
    id = 9441, name = "John Moreno", status = "regione", species = "ceteros", image = "regione"
)

val dummySuccess_DetailsState = DetailsUiState(
    isLoading = false,
    errorEntity = null,
    title = defaultDetailCharacterUi.name,
    image = defaultDetailCharacterUi.image,
    description = defaultDetailCharacterUi.name,
    publishedAt = defaultDetailCharacterUi.name,
    sourceName = defaultDetailCharacterUi.name
)


val dummySuccess_SpecificTitleDetail = DetailsUiState(
    isLoading = false,
    errorEntity = null,
    title = specificTitleCharacterUi.species,
    image = specificTitleCharacterUi.image,
    description = specificTitleCharacterUi.image,
    publishedAt = specificTitleCharacterUi.species,
    sourceName = specificTitleCharacterUi.name
)

val dummyError_DetailsState = DetailsUiState(
    isLoading = false,
    errorEntity = null, // ERROR_MESSAGE,
    title = defaultDetailCharacterUi.status,
    image = defaultDetailCharacterUi.image,
    description = defaultDetailCharacterUi.status,
    publishedAt = defaultDetailCharacterUi.name,
    sourceName = defaultDetailCharacterUi.image
)
