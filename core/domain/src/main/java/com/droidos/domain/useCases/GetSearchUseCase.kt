package com.droidos.domain.useCases

import com.droidos.domain.repository.SearchCharacterRepository

class GetSearchUseCase(
    private val searchCharacterRepository: SearchCharacterRepository,
) {
    /**
     * Executes the search character use case.
     *
     * @param name The name of the character to search for.
     * @return The result of the search operation.
     */
    suspend operator fun invoke(name: String) = searchCharacterRepository.searchCharacters(name)
}
