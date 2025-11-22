package com.droidos.design.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.droidos.network.di.errorHandler.entities.ErrorEntity

@Composable
fun HandleError(
    snackBarHostState: SnackbarHostState,
    error: ErrorEntity?,
    onDismiss: () -> Unit,
) {
    error?.let {
        when (it) {
            ErrorEntity.Network,
            ErrorEntity.Timeout,
            -> {
                LaunchedEffect(it) {
                    snackBarHostState.showSnackbar(
                        message = "Check your internet connection",
                        withDismissAction = true,
                    )
                    onDismiss()
                }
            }

            ErrorEntity.Unauthorized,
            ErrorEntity.AccessDenied,
            -> {
                DialogWrapper("You are not authorized to access this content.", onDismiss)
            }

            ErrorEntity.BadRequest -> {
                DialogWrapper("Bad request sent to the server.", onDismiss)
            }

            ErrorEntity.NotFound -> {
                LaunchedEffect(it) {
                    snackBarHostState.showSnackbar(
                        message = "The content you are looking for was not found.",
                        withDismissAction = true,
                    )
                    onDismiss()
                }
            }

            ErrorEntity.ServiceUnavailable,
            ErrorEntity.ServerInternalError,
            ErrorEntity.GatewayError,
            -> {
                DialogWrapper("Server is currently unavailable. Please try again later.", onDismiss)
            }

            is ErrorEntity.Unknown -> {
                LaunchedEffect(it) {
                    val msg = it.message ?: "Unknown error occurred"
                    snackBarHostState.showSnackbar(msg, withDismissAction = true)
                    onDismiss()
                }
            }
        }
    }
}

@Composable
fun DialogWrapper(
    message: String,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Error") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        },
    )
}
