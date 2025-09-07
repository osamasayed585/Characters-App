package com.yassir.home.event

import com.yassir.common.base.ScreenEvent


sealed class HomeEvents : ScreenEvent {
    data class OnQueryChange(val query: String) : HomeEvents()
}