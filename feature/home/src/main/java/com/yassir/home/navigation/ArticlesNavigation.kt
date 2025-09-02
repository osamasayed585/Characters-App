package com.yassir.home.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.yassir.common.utils.Screen
import com.yassir.home.CharactersRoute


fun NavGraphBuilder.charactersScreen(
    onNavToDetails: (String) -> Unit,
) {
    composable(Screen.HomeScreen.route) {
        CharactersRoute(onNavToDetails)
    }
}

