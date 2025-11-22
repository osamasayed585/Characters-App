@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.droidos.design.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import com.droidos.common.utils.Screen
import com.droidos.design.R

@Composable
fun RMCTopBar(
    destination: NavDestination?,
    querySearch: String,
    onSearchClick: (String) -> Unit,
    onBackClick: () -> Unit,
) {
    // Keeps search open when returning from details screen,
    // Without this, search would close after navigation
    var isSearchExpanded by remember { mutableStateOf(false) }

    @Composable
    fun HomeTopBar() {
        CustomHomeTopAppBar(
            query = querySearch,
            onQueryChange = onSearchClick,
            isSearchExpanded = isSearchExpanded,
            onExpandedChange = {
                isSearchExpanded = it
            },
        )
    }

    @Composable
    fun DetailTopBar() {
        TopAppBar(
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                ),
            title = {
                Text(text = stringResource(R.string.detail))
            },
            navigationIcon = {
                IconButton(onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                    )
                }
            },
        )
    }

    when (destination?.route) {
        Screen.HomeScreen.route -> HomeTopBar()
        Screen.DetailScreen.route -> DetailTopBar()
    }
}
