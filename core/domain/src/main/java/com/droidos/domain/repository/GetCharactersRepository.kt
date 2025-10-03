package com.droidos.domain.repository

import androidx.paging.PagingData
import com.droidos.model.beans.CharacterUIModel
import kotlinx.coroutines.flow.Flow

interface GetCharactersRepository {

    /**
     * Requests a [Flow] of [PagingData] for [CharacterUIModel] objects.
     *
     * This function is responsible for fetching character data, typically from a remote or local data source,
     * and presenting it in a paginated format suitable for UI display.
     *
     * @return A [Flow] emitting [PagingData] of [CharacterUIModel].
     */
    fun fetchCharacters(): Flow<PagingData<CharacterUIModel>>
}