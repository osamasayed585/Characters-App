package com.droidos.home.actions

import com.droidos.common.base.ScreenAction

sealed class HomeActions : ScreenAction {
    data class OnQueryChange(
        val query: String,
    ) : HomeActions()
}
