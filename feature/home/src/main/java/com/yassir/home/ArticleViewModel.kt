package com.yassir.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yassir.common.di.DispatcherProvider
import com.yassir.domain.useCases.GetCharacterUseCase
import com.yassir.model.beans.CharacterUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(
    private val articlesUseCase: GetCharacterUseCase,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private val _characters = MutableStateFlow<PagingData<CharacterUIModel>>(PagingData.empty())
    val characters: StateFlow<PagingData<CharacterUIModel>> = _characters.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = PagingData.empty(),
    )

    init {
        requestArticles()
    }


    fun requestArticles() {
        viewModelScope.launch(dispatcherProvider.main) {
            articlesUseCase.invoke()
                .flowOn(dispatcherProvider.io)
                .cachedIn(viewModelScope)
                .collect(_characters::tryEmit)
        }
    }


}