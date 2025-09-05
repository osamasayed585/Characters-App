package com.yassir.home

import androidx.paging.PagingData
import com.yassir.domain.repository.GetCharactersRepository
import com.yassir.model.beans.CharacterUIModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FakeGetCharactersRepositoryImp : GetCharactersRepository {

    private var charactersList: List<CharacterUIModel> = emptyList()
    private var shouldThrowError: Boolean = false

    fun setCharacters(characters: List<CharacterUIModel>) {
        this.charactersList = characters
    }

    fun setShouldThrowError(shouldThrow: Boolean) {
        this.shouldThrowError = shouldThrow
    }

    override fun requestCharacters(): Flow<PagingData<CharacterUIModel>> {
        return if (shouldThrowError) {
            flow { throw RuntimeException("Test error") }
        } else {
            flowOf(PagingData.from(charactersList))
        }
    }
}