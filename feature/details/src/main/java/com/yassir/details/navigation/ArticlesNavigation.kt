package com.yassir.details.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.yassir.common.utils.Screen
import com.yassir.details.ArticleDetailsRoute


fun NavGraphBuilder.characterDetailsScreen(snackbarHostState: SnackbarHostState) {
    composable(
        route = Screen.DetailScreen.route + "/{${""}}",
        arguments = listOf(navArgument("") { type = NavType.StringType })
    ) {
        ArticleDetailsRoute(snackbarHostState)
    }
}


fun NavController.navigateToCharacterDetails(title: String) =
    navigate(Screen.DetailScreen.route + "/${title}")
