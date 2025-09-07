package com.yassir.home.actions

import com.yassir.common.base.ScreenAction


sealed class HomeActions : ScreenAction {
    data class OnQueryChange(val query: String) : HomeActions()
}