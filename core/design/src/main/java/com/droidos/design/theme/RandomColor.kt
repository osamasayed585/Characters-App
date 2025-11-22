package com.droidos.design.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

private val lightPalette =
    listOf(
        random1Light,
        random2Light,
        random3Light,
        random4Light,
        random5Light,
    )

private val darkPalette =
    listOf(
        random1Dark,
        random2Dark,
        random3Dark,
        random4Dark,
        random5Dark,
    )

@Composable
fun randomColor(): Color {
    val isDarkTheme = isSystemInDarkTheme()
    return remember {
        val colors = if (isDarkTheme) darkPalette else lightPalette
        colors.random()
    }
}
