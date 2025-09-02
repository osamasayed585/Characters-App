package com.yassir.design.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.yassir.design.R

/**
 * A composable function that displays an image from a given URL with a loading placeholder.

 * @param modifier Modifier used to adjust the layout or appearance of the image.
 * @param url The URL of the image to be displayed.
 * @param description The content description for the image, used for accessibility purposes.
 */
@Composable
fun LoadingImage(
    modifier: Modifier = Modifier,
    url: String,
    description: String,
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.80f),
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(url)
            .crossfade(true)
            .placeholder(R.drawable.loading)
            .build(),
        contentDescription = description,
        contentScale = ContentScale.Crop
    )
}
