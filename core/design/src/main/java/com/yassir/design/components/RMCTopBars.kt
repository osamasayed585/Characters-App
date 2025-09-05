@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.yassir.design.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import com.yassir.common.utils.Screen
import com.yassir.design.R


@Composable
fun RMCTopBar(
    destination: NavDestination?,
    querySearch: String,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    when (destination?.route) {
        Screen.HomeScreen.route -> HomeTopBar(querySearch, onSearchClick)
        Screen.DetailScreen.route -> CharacterDetailTopBar(onBackClick)
    }
}

@Composable
fun HomeTopBar(querySearch: String, onSearchClick: (String) -> Unit) {
    CenterAlignedTopAppBar(
        title = { CustomHomeTopAppBar(querySearch, onSearchClick) }
    )
}

@Composable
fun CharacterDetailTopBar(onBackClick: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
            navigationIconContentColor = MaterialTheme.colorScheme.onBackground
        ),
        title = {
            Text(text = stringResource(R.string.detail))

        },
        navigationIcon = {
            IconButton(onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }
        }
    )
}
