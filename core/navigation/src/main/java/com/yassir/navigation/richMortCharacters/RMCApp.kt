package com.yassir.navigation.richMortCharacters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yassir.common.R
import com.yassir.navigation.util.networkMonitor.NetworkMonitor
import com.yassir.design.components.RMCTopBar

@Composable
fun RMCApp(
    networkMonitor: NetworkMonitor,
    navController: NavHostController,
    appState: RMCState = rememberJetNewsState(
        networkMonitor = networkMonitor,
        navController = navController
    ),
) {
    val notConnectedMessage = stringResource(R.string.not_connected)
    val snackbarHostState = remember { SnackbarHostState() }
    val isOffline by appState.isOffline.collectAsStateWithLifecycle()

    /**
     *  Show a snackbar whenever there's a connection issue.,
     */
    LaunchedEffect(isOffline) {
        if (isOffline) {
            snackbarHostState.showSnackbar(
                message = notConnectedMessage,
                duration = SnackbarDuration.Indefinite,
            )
        }
    }

    Scaffold(
        topBar = {
            RMCTopBar(appState.currentDestination)
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            RMCNavHost(navController, snackbarHostState)
        }
    }

}