package com.yassir.navigation.richMortCharacters

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.yassir.common.utils.Screen
import com.yassir.details.navigation.characterDetailsScreen
import com.yassir.details.navigation.navigateToCharacterDetails
import com.yassir.home.navigation.charactersScreen

@Composable
fun RMCNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    querySearch: String
) {
    NavHost(
        navController = navController,
        startDestination = Screen.HomeScreen.route
    ) {
        charactersScreen(navController::navigateToCharacterDetails, querySearch)

        characterDetailsScreen(snackbarHostState)
    }
}