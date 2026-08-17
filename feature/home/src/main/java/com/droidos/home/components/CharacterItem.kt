package com.droidos.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.droidos.design.components.LoadingImage
import com.droidos.design.theme.AppPreview
import com.droidos.design.theme.RMCTheme
import com.droidos.model.CharacterModel

@Composable
fun CharacterItem(
    uiState: CharacterModel,
    onClick: () -> Unit,
) {
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable { onClick() },
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

@AppPreview
@Composable
private fun CharacterItemPreview() {
    RMCTheme {
        CharacterItem(
            uiState =
                CharacterModel(
                    id = 1,
                    name = "Morty Smith",
                    image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                    species = "Alive",
                    status = "Human",
                ),
            onClick = {},
        )
    }
}
