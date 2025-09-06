package com.yassir.details

import com.yassir.details.state.DetailsUiState
import com.yassir.details.state.DetailsUiState.DetailApiState
import com.yassir.model.beans.CharacterUIModel
import com.yassir.network.di.errorHandler.entities.ErrorEntity
import io.mockk.every
import io.mockk.mockk

const val ERROR_MESSAGE = "Error Message"

val errorDetailsUiState = DetailsUiState(
    id = -1,
    name = "",
    status = "",
    species = "",
    image = "",
    apiState =  DetailApiState.Error(ErrorEntity.Unknown(ERROR_MESSAGE)),
    errorEntity = ErrorEntity.Unknown(ERROR_MESSAGE)
)


fun createMockCharacter(
    id: Int = 1,
    name: String = "Rick Sanchez",
    status: String = "Alive",
    species: String = "Human",
    image: String = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
): CharacterUIModel {
    return mockk {
        every { this@mockk.id } returns id
        every { this@mockk.name } returns name
        every { this@mockk.status } returns status
        every { this@mockk.species } returns species
        every { this@mockk.image } returns image
    }
}

