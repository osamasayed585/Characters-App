package com.yassir.details

import com.yassir.domain.repository.CharacterDetailsRepository
import com.yassir.model.beans.CharacterUIModel

class FakeArticleDetailsRepositoryImp : CharacterDetailsRepository {
    override suspend fun requestCharacterDetails(query: String): Result<CharacterUIModel> {
        return when(query){
            SPECIFIC_TITLE -> Result.success(defaultDetailArticleUi)
            else -> Result.failure(IllegalStateException(ERROR_MESSAGE))
        }
    }
}