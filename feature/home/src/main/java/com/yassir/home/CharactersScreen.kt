package com.yassir.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.yassir.common.utils.Constants.DESCRIPTION_MAX_LINES
import com.yassir.common.utils.Constants.TITLE_MAX_LINES
import com.yassir.design.components.ErrorScreen
import com.yassir.design.components.FullScreenLoading
import com.yassir.design.components.LoadingImage
import com.yassir.design.theme.RMCTheme
import com.yassir.model.beans.CharacterUIModel
import timber.log.Timber


@Composable
fun CharactersRoute(
    onNavToDetails: (String) -> Unit,
    viewModel: ArticleViewModel = hiltViewModel(),
) {
    val characters = viewModel.characters.collectAsLazyPagingItems()
    val characterState: LazyListState = rememberLazyListState()

    CharactersScreen(
        characters = characters,
        characterState = characterState,
        onNavToDetails = onNavToDetails
    )
}

@Composable
fun CharactersScreen(
    characters: LazyPagingItems<CharacterUIModel>,
    characterState: LazyListState,
    onNavToDetails: (String) -> Unit,
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = characterState
    ) {
        items(count = characters.itemCount) {
            characters[it]?.let { character ->
                CharacterItem(
                    uiState = character,
                    onCLick = { onNavToDetails(character.name) }
                )
            }
        }

        item {
            characters.apply {
                when (loadState.append) {
                    is LoadState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                                .size(60.dp),
                            color = Color.Red,
                            strokeWidth = 2.dp
                        )
                    }

                    is LoadState.NotLoading -> {}

                    is LoadState.Error -> {
                        val error = characters.loadState.append as LoadState.Error
                        Timber.e("ArticlesScreen: ${error.error.localizedMessage}")
                        LimitedCard()
                    }
                }
            }
        }
    }

    characters.apply {
        when (loadState.refresh) {
            is LoadState.Loading -> {
                FullScreenLoading(modifier = Modifier.fillMaxSize())
            }

            is LoadState.NotLoading -> {}

            is LoadState.Error -> {
                val error = loadState.refresh as LoadState.Error
                Timber.e("requestProducts: ${error.error.message}")
                ErrorScreen(
                    modifier = Modifier.fillMaxSize(),
                    onRetry = { retry() }
                )
            }

        }
    }

}

@Composable
fun CharacterItem(uiState: CharacterUIModel, onCLick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCLick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {

        LoadingImage(
            modifier = Modifier.fillMaxWidth(),
            url = uiState.image,
            description = stringResource(com.yassir.common.R.string.this_is_an_image_of_character),
        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = uiState.name,
            maxLines = TITLE_MAX_LINES,
            style = MaterialTheme.typography.titleMedium

        )

        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = uiState.species,
            maxLines = DESCRIPTION_MAX_LINES,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ArticleItemPreview_Light() {
    RMCTheme {
        CharacterItem(
            uiState = CharacterUIModel(
                id = 1,
                name = "Netanyahu to address US Congress on 24 July",
                image = "integer",
                species = "Why are people in China buying salt and Why are people in China buying salt?",
                status = "2024-07-17T10:40:00Z"
            ),
            onCLick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
)
@Composable
fun ArticleItemPreview_Dark() {
    RMCTheme {

        CharacterItem(
            uiState = CharacterUIModel(
                id = 1,
                name = "Netanyahu to address US Congress on 24 July",
                image = "integer",
                species = "Why are people in China buying salt and Why are people in China buying salt?",
                status = "2024-07-17T10:40:00Z"
            ),
            onCLick = {}
        )
    }
}

@Composable
private fun LimitedCard() {

    Card {
        Text(
            modifier = Modifier.padding(16.dp),
            text = stringResource(R.string.limited_message)
        )

    }
}