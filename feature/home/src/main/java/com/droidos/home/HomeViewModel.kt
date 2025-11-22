@file:OptIn(ExperimentalCoroutinesApi::class)

package com.droidos.home

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.droidos.common.base.BaseViewModel
import com.droidos.domain.useCases.CharactersUseCases
import com.droidos.home.actions.HomeActions
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.beans.CharacterUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val charactersUseCases: CharactersUseCases,
    ) : BaseViewModel<HomeUiState, HomeActions>(HomeUiState()) {
        val characters: StateFlow<PagingData<CharacterUIModel>> =
            uiState
                .map { it.searchQuery }
                .debounce(500)
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    if (query.isBlank()) {
                        charactersUseCases.getCharactersUseCase()
                    } else {
                        charactersUseCases.getSearchUseCase(query)
                    }
                }.cachedIn(viewModelScope)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = PagingData.empty(),
                )

        override fun processAction(
            oldState: HomeUiState,
            action: HomeActions,
        ) {
            when (action) {
                is HomeActions.OnQueryChange -> {
                    updateState { copy(searchQuery = action.query) }
                }
            }
        }
    }
