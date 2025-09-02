package com.yassir.details.state

import com.yassir.common.base.ScreenState
import com.yassir.network.di.errorHandler.entities.ErrorEntity

data class DetailsUiState(
    val isLoading: Boolean = true,
    val errorEntity: ErrorEntity? = null,
    val title: String = "",
    val image: String = "",
    val description: String = "",
    val publishedAt: String = "",
    val sourceName: String = "",
) : ScreenState