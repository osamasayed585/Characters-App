package com.droidos.details

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.droidos.design.components.DetailRow
import com.droidos.design.components.ErrorCard
import com.droidos.design.components.FullScreenLoading
import com.droidos.design.components.HandleError
import com.droidos.design.theme.RMCTheme
import com.droidos.design.theme.randomColor
import com.droidos.details.actions.DetailsAction
import com.droidos.details.state.DetailsUiState
import com.droidos.network.di.errorHandler.entities.ErrorEntity

@Composable
fun CharacterDetailsRoute(snackbarHostState: SnackbarHostState) {
    val viewModel: DetailsViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    HandleError(
        snackBarHostState = snackbarHostState,
        error = uiState.errorEntity,
        onDismiss = { viewModel.emitAction(DetailsAction.ClearError) },
    )

    CharacterDetails(
        uiState = uiState,
        onRetry = {
            viewModel.fetchCharacterDetails(uiState.id)
        },
    )
}

@Composable
fun CharacterDetails(
    uiState: DetailsUiState,
    onRetry: () -> Unit,
) {
    when (uiState.apiState) {
        is DetailsUiState.DetailApiState.Error -> ErrorCard { onRetry() }
        is DetailsUiState.DetailApiState.Loading -> FullScreenLoading()
        is DetailsUiState.DetailApiState.Success -> {
            CharacterDetailsContent(uiState)
        }
    }
}

@Composable
fun CharacterDetailsContent(
    uiState: DetailsUiState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(randomColor()),
        )

        AsyncImage(
            model = uiState.image,
            contentDescription = "${uiState.name}'s image",
            modifier =
                Modifier
                    .offset(y = (-80).dp)
                    .size(160.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .border(4.dp, MaterialTheme.colorScheme.outline, CircleShape),
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = (-80).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = uiState.name,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )

            Card(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    DetailRow(
                        label = stringResource(R.string.species),
                        value = uiState.species,
                    )
                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))
                    DetailRow(
                        label = stringResource(R.string.status),
                        value = uiState.status,
                    )
                }
            }
        }
    }
}

@Preview(
    name = "Character Details - Success (Light)",
    showBackground = true,
)
@Preview(
    name = "Character Details - Success (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PreviewCharacterDetailsSuccess() {
    RMCTheme {
        val sampleCharacter =
            DetailsUiState(
                id = 1,
                name = "Rick Sanchez",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                species = "Human",
                status = "Alive",
                apiState = DetailsUiState.DetailApiState.Success,
            )

        CharacterDetails(
            uiState = sampleCharacter,
            onRetry = {},
        )
    }
}

@Preview(
    name = "Character Details - Loading (Light)",
    showBackground = true,
)
@Preview(
    name = "Character Details - Loading (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PreviewCharacterDetailsLoading() {
    RMCTheme {
        CharacterDetails(
            uiState = DetailsUiState(apiState = DetailsUiState.DetailApiState.Loading),
            onRetry = {},
        )
    }
}

@Preview(
    name = "Character Details - Error (Light)",
    showBackground = false,
)
@Preview(
    name = "Character Details - Error (Dark)",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun PreviewCharacterDetailsError() {
    RMCTheme {
        CharacterDetails(
            uiState =
                DetailsUiState(
                    apiState =
                        DetailsUiState.DetailApiState.Error(
                            ErrorEntity.Unknown("Failed to load character data."),
                        ),
                ),
            onRetry = {},
        )
    }
}
