package com.yassir.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yassir.common.utils.Screen
import com.yassir.home.HomesRoute


fun NavGraphBuilder.charactersScreen(
    onNavToDetails: (Int) -> Unit,
    querySearch: String
) {
    composable(Screen.HomeScreen.route) {
        HomesRoute(onNavToDetails, querySearch)
    }
}

