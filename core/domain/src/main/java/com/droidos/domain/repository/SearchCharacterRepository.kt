package com.droidos.domain.repository

import androidx.paging.PagingData
import com.droidos.model.beans.CharacterUIModel
import kotlinx.coroutines.flow.Flow

interface SearchCharacterRepository {
    /**
     * Searches for a character by name.
     *
     * This function is responsible for finding a specific character based on the provided name.
     * It will typically query a data source (remote or local) for a character matching the given name.
     *
     * @param name The name of the character to search for.
     * @return A [CharacterUIModel] representing the found character.
     * @throws NoSuchElementException if no character with the specified name is found.
     */
    suspend fun searchCharacters(name: String): Flow<PagingData<CharacterUIModel>>
}
