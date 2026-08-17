package com.droidos.domain.repository

import com.droidos.model.CharacterModel

interface CharacterDetailsRepository {
    suspend fun requestCharacterDetails(id: Int): Result<CharacterModel>
}
