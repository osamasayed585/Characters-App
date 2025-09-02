@file:OptIn(ExperimentalMaterial3Api::class)

package com.yassir.design.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import com.yassir.common.utils.Screen
import com.yassir.design.R


@Composable
fun RMCTopBar(
    destination: NavDestination?,
) {
    when (destination?.route) {
        Screen.HomeScreen.route -> HomeTopBar()
        Screen.DetailScreen.route -> ArticleDetailTopBar()
    }
}

@Composable
fun HomeTopBar() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = stringResource(R.string.r_m_c),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun ArticleDetailTopBar() {

    TopAppBar(
        title = { Text(text = stringResource(R.string.detail)) },
        navigationIcon = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }
    )
}
