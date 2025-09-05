package com.yassir.domain.useCases

import com.yassir.domain.repository.CharacterDetailsRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetCharacterDetailsUseCase @Inject constructor(
    private val repository: CharacterDetailsRepository,
) {

    suspend operator fun invoke(id: Int) = repository.requestCharacterDetails(id)
}