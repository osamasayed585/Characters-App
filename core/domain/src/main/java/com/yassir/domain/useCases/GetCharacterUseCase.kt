package com.yassir.domain.useCases

import androidx.paging.PagingData
import com.yassir.domain.repository.GetCharactersRepository
import com.yassir.model.beans.CharacterUIModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ViewModelScoped
class GetCharacterUseCase @Inject constructor(
    private val getCharactersRepository: GetCharactersRepository,
) {


    /**
     * Fetches a flow of paged characters.
     *
     * This function retrieves characters from the `charactersRepository`.
     *
     * @return A flow of PagingData containing the characters.
     */
    operator fun invoke(): Flow<PagingData<CharacterUIModel>> {
        return getCharactersRepository.requestCharacters()
    }

}