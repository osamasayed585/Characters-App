package com.droidos.domain.repository

import androidx.paging.PagingData
import com.droidos.model.CharacterModel
import kotlinx.coroutines.flow.Flow

interface GetCharactersRepository {
    /**
     * Requests a [Flow] of [PagingData] for [CharacterModel] objects.
     *
     * This function is responsible for fetching character data, typically from a remote or local data source,
     * and presenting it in a paginated format suitable for UI display.
     *
     * @return A [Flow] emitting [PagingData] of [CharacterModel].
     */
    fun fetchCharacters(): Flow<PagingData<CharacterModel>>
}
