@file:OptIn(ExperimentalCoroutinesApi::class)

package com.yassir.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yassir.common.base.BaseViewModel
import com.yassir.domain.useCases.CharactersUseCases
import com.yassir.home.event.HomeEvents
import com.yassir.home.uiState.HomeUiState
import com.yassir.model.beans.CharacterUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val charactersUseCases: CharactersUseCases,
) : BaseViewModel<HomeUiState, HomeEvents>(HomeUiState()) {

    private val searchQuery = MutableStateFlow("")
    val characters: StateFlow<PagingData<CharacterUIModel>> = searchQuery
        .debounce(500)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) charactersUseCases.getCharactersUseCase()
            else charactersUseCases.getSearchUseCase(query)
        }
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PagingData.empty()
        )

    override fun reduce(oldState: HomeUiState, sideEffect: HomeEvents) {
        when (sideEffect) {
            is HomeEvents.OnQueryChange -> {
                updateState { copy(searchQuery = sideEffect.query) }
                searchQuery.update { sideEffect.query }
            }
        }
    }
}