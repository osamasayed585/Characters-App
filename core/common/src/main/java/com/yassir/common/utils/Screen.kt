package com.yassir.common.utils

sealed class Screen(val route: String) {
    data object HomeScreen : Screen("homeScreen")
    data object DetailScreen : Screen("detailScreen")
}
