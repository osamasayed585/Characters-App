package com.droidos.navigation.richMortCharacters

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.droidos.common.utils.Screen
import com.droidos.details.navigation.characterDetailsScreen
import com.droidos.details.navigation.navigateToCharacterDetails
import com.droidos.home.navigation.charactersScreen

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