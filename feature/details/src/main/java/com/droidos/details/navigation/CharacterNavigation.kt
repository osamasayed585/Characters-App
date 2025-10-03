package com.droidos.details.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.droidos.common.utils.Constants
import com.droidos.common.utils.Screen
import com.droidos.details.CharacterDetailsRoute


fun NavGraphBuilder.characterDetailsScreen(snackbarHostState: SnackbarHostState) {
    composable(
        route = Screen.DetailScreen.route,
        arguments = listOf(navArgument(Constants.ID) { type = NavType.IntType })
    ) {
        CharacterDetailsRoute(snackbarHostState)
    }
}


fun NavController.navigateToCharacterDetails(id: Int) =
    navigate(Screen.DetailScreen.createRoute(id))
