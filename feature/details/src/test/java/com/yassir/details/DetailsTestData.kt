package com.yassir.details

import com.yassir.details.state.DetailsUiState
import com.yassir.model.beans.CharacterUIModel

const val ERROR_MESSAGE = "Error message"
const val SPECIFIC_TITLE = "the specific title"


val defaultDetailArticleUi = CharacterUIModel(
    id = 9441, name = "John Moreno", status = "regione", species = "ceteros", image = "regione"

)

val specificTitleArticleUi = CharacterUIModel(
    id = 9441, name = "John Moreno", status = "regione", species = "ceteros", image = "regione"
)

val dummySuccess_DetailsState = DetailsUiState(
    isLoading = false,
    errorEntity = null,
    title = defaultDetailArticleUi.name,
    image = defaultDetailArticleUi.image,
    description = defaultDetailArticleUi.name,
    publishedAt = defaultDetailArticleUi.name,
    sourceName = defaultDetailArticleUi.name
)


val dummySuccess_SpecificTitleDetail = DetailsUiState(
    isLoading = false,
    errorEntity = null,
    title = specificTitleArticleUi.species,
    image = specificTitleArticleUi.image,
    description = specificTitleArticleUi.image,
    publishedAt = specificTitleArticleUi.species,
    sourceName = specificTitleArticleUi.name
)

val dummyError_DetailsState = DetailsUiState(
    isLoading = false,
    errorEntity = null, // ERROR_MESSAGE,
    title = defaultDetailArticleUi.status,
    image = defaultDetailArticleUi.image,
    description = defaultDetailArticleUi.status,
    publishedAt = defaultDetailArticleUi.name,
    sourceName = defaultDetailArticleUi.image
)
