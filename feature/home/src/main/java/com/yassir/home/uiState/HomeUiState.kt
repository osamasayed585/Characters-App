package com.yassir.home.uiState

import com.yassir.common.base.ScreenState

data class HomeUiState(
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
): ScreenState
