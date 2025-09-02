package com.yassir.design.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yassir.design.R
import com.yassir.design.theme.RMCTheme


/**
 * Composable function that displays an error screen with a retry button.
 *
 * @param modifier Modifier used to adjust the layout of the composable.
 * @param onRetry Callback function to be executed when the retry button is clicked.
 */
@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    onRetry: () -> Unit,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_error),
            contentDescription = stringResource(R.string.unexpected_error_image)
        )

        Text(
            modifier = Modifier.padding(top = 32.dp),
            text = stringResource(R.string.unexpected_error_msg),
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            modifier = Modifier.padding(top = 8.dp),
            onClick = { onRetry() }
        ) {
            Text(stringResource(R.string.retry))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ErrorScreenPreview() {
    RMCTheme {
        ErrorScreen(
            modifier = Modifier.fillMaxSize(),
            onRetry = {}
        )
    }
}