package com.droidos.details.state

import androidx.compose.runtime.Stable
import com.droidos.common.base.ScreenState
import com.droidos.network.di.errorHandler.entities.ErrorEntity

@Stable
data class DetailsUiState(
    val id: Int = -1,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val image: String = "",
    val apiState: DetailApiState = DetailApiState.Loading,
    val errorEntity: ErrorEntity? = null,
) : ScreenState {
    sealed class DetailApiState {
        data object Success : DetailApiState()

        data object Loading : DetailApiState()

        data class Error(
            val errorEntity: ErrorEntity,
        ) : DetailApiState()
    }
}
