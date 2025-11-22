package com.droidos.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.droidos.common.utils.Screen
import com.droidos.home.HomesRoute

fun NavGraphBuilder.charactersScreen(
    onNavToDetails: (Int) -> Unit,
    querySearch: String,
) {
    composable(Screen.HomeScreen.route) {
        HomesRoute(onNavToDetails, querySearch)
    }
}
