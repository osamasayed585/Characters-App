package com.droidos.domain.repository

import com.droidos.model.beans.CharacterUIModel


interface CharacterDetailsRepository {

    suspend fun requestCharacterDetails(id: Int): Result<CharacterUIModel>
}