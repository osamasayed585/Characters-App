package com.yassir.details

import com.yassir.details.state.DetailsUiState
import com.yassir.details.state.DetailsUiState.DetailApiState
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorEntity

const val ERROR_MESSAGE = "Error message"



val TestDetailCharacterUi = CharacterUIModel(
    id = 9441, name = "Osama Sayed", status = "Live", species = "ceteros", image = "regione"
)

val defaultDetailCharacterUi = CharacterUIModel(
    id = -1, name = "", status = "", species = "", image = ""
)


val errorDetailsUiState = DetailsUiState(
    id = -1,
    name = "",
    status = "",
    species = "",
    image = "",
    apiState =  DetailApiState.Error(ErrorEntity.Unknown("Error message")),
    errorEntity = ErrorEntity.Unknown("Error message")
)


val dummySuccess = DetailsUiState(
    errorEntity = null,
    apiState = DetailApiState.Success,
    name = TestDetailCharacterUi.name,
    image = TestDetailCharacterUi.image,
    status = TestDetailCharacterUi.status,
    species = TestDetailCharacterUi.species,
)