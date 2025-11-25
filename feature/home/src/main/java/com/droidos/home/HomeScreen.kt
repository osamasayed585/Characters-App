package com.droidos.home

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.droidos.design.components.ErrorCard
import com.droidos.design.components.LoadingImage
import com.droidos.design.theme.RMCTheme
import com.droidos.home.actions.HomeActions
import com.droidos.home.uiState.HomeUiState
import com.droidos.model.beans.CharacterUIModel
import retrofit2.HttpException

@Composable
fun HomesRoute(
    onNavToDetails: (Int) -> Unit,
    querySearch: String,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val characterState: LazyListState = rememberLazyListState()
    val uiState: HomeUiState by viewModel.uiState.collectAsState()

    LaunchedEffect(querySearch) {
        viewModel.emitAction(HomeActions.OnQueryChange(querySearch))
    }

    HomeScreen(
        uiState = uiState,
        characters = characters,
        characterState = characterState,
        onNavToDetails = onNavToDetails,
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    characters: LazyPagingItems<CharacterUIModel>,
    characterState: LazyListState,
    onNavToDetails: (Int) -> Unit,
) {
    val refreshState = characters.loadState.refresh
    val appendState = characters.loadState.append

    LazyColumn(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = characterState,
    ) {
        if (refreshState is LoadState.Loading) {
            items(10) {
                CharacterShimmerItem()
            }
        } else if (refreshState is LoadState.Error) {
            val error = refreshState.error
            item {
                if (error is HttpException && error.code() == 404) {
                    NoResultsPlaceholder(
                        message =
                            stringResource(
                                R.string.no_characters_found_for,
                                uiState.searchQuery,
                            ),
                    )
                } else {
                    ErrorCard {
                        characters.refresh()
                    }
                }
            }
        } else {
            if (characters.itemCount == 0) {
                item {
                    NoResultsPlaceholder(
                        message =
                            stringResource(
                                R.string.no_characters_found_for,
                                uiState.searchQuery,
                            ),
                    )
                }
            } else {
                items(count = characters.itemCount) { index ->
                    characters[index]?.let { character ->
                        CharacterItem(
                            uiState = character,
                            onCLick = { onNavToDetails(character.id) },
                        )
                    }
                }

                if (appendState is LoadState.Loading) {
                    items(10) {
                        CharacterShimmerItem()
                    }
                } else if (appendState is LoadState.Error) {
                    item {
                        ErrorCard {
                            characters.retry()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItem(
    uiState: CharacterUIModel,
    onCLick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onCLick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            LoadingImage(
                modifier =
                    Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                url = uiState.image,
                description = "${uiState.name}'s image",
            )

            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = uiState.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = uiState.species,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                )
            }
        }
    }
}

@Composable
fun CharacterShimmerItem() {
    val shimmerColors =
        listOf(
            Color.LightGray.copy(alpha = 0.6f),
            Color.LightGray.copy(alpha = 0.2f),
            Color.LightGray.copy(alpha = 0.6f),
        )

    val transition = rememberInfiniteTransition(label = "")
    val translateAnim =
        transition.animateFloat(
            initialValue = 0f,
            targetValue = 1000f,
            animationSpec =
                infiniteRepeatable(
                    animation = tween(1200, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart,
                ),
            label = "",
        )

    val brush =
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim.value, translateAnim.value),
            end = Offset(translateAnim.value + 200f, translateAnim.value + 200f),
        )

    Card(
        modifier =
            Modifier
                .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier =
                    Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(brush),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier =
                        Modifier
                            .height(20.dp)
                            .fillMaxWidth(0.4f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush),
                )

                Box(
                    modifier =
                        Modifier
                            .height(14.dp)
                            .fillMaxWidth(0.25f)
                            .clip(RoundedCornerShape(4.dp))
                            .background(brush),
                )
            }
        }
    }
}

@Composable
fun NoResultsPlaceholder(message: String) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(64.dp),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun CharacterItemPreview_Light() {
    RMCTheme {
        CharacterItem(
            uiState =
                CharacterUIModel(
                    id = 1,
                    name = "Morty Smith",
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    species = "Alive",
                    status = "Human",
                ),
            onCLick = {},
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL,
)
@Composable
fun CharacterItemPreview_Dark() {
    RMCTheme {
        CharacterItem(
            uiState =
                CharacterUIModel(
                    id = 1,
                    name = "Morty Smith",
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    species = "Alive",
                    status = "Human",
                ),
            onCLick = {},
        )
    }
}
