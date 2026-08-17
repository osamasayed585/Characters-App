package com.droidos.design.theme

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

/**
 * Single place to change which locale every preview renders in. The app does not force a
 * locale at runtime, so this is the platform default; flip it here once and every
 * [AppPreview]/[LightPreview]/[DarkPreview] user picks it up.
 */
const val PREVIEW_LOCALE = "en"

@Preview(
    name = "Light",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
    locale = PREVIEW_LOCALE,
)
annotation class LightPreview

@Preview(
    name = "Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
    locale = PREVIEW_LOCALE,
)
annotation class DarkPreview

/** Default annotation for screen previews — renders Light + Dark from one function. */
@LightPreview
@DarkPreview
annotation class AppPreview
