package com.yassir.details

import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.model.beans.CharacterUIModel

class FakeCharacterDetailsRepositoryImp : CharacterDetailsRepository {
    override suspend fun requestCharacterDetails(id: Int): Result<CharacterUIModel> {
        return when(id){
            1 -> Result.success(defaultDetailCharacterUi)
            else -> Result.failure(IllegalStateException(ERROR_MESSAGE))
        }
    }
}