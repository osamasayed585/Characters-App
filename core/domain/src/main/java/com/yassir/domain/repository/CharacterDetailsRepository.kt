package com.yassir.domain.repository

import com.yassir.model.beans.CharacterUIModel


interface CharacterDetailsRepository {

    suspend fun requestCharacterDetails(query: String): Result<CharacterUIModel>
}