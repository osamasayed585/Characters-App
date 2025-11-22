package com.droidos.home.uiState

import com.droidos.common.base.ScreenState

data class HomeUiState(
    val searchQuery: String = "",
) : ScreenState
