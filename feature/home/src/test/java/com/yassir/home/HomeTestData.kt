package com.yassir.home

import com.yassir.model.beans.ArticleUIModel

const val SPECIFIC_TITLE = "Title1"

val dummySuccess_SpecificTitleDetail = ArticleUIModel(
    title = SPECIFIC_TITLE,
    image = "Image1",
    description = "Description1",
    publishedAt = "2023-01-01",
    sourceName = "name"
)


val dummySuccess_HomeState = listOf(
    ArticleUIModel("Title1", "Image1", "Description1", "2023-01-01", "Source1"),
    ArticleUIModel("Title2", "Image2", "Description2", "2023-01-02", "Source2"),
    ArticleUIModel("Title3", "Image3", "Description3", "2023-01-02", "Source3"),
    ArticleUIModel("Title4", "Image4", "Description4", "2023-01-02", "Source4"),
)

val remove_dummySuccess_HomeState = listOf(
    ArticleUIModel("Title1", "Image1", "Description1", "2023-01-01", "Source1"),
    ArticleUIModel("Title3", "Image3", "Description3", "2023-01-02", "Source3"),
)

val changes_dummySuccess_HomeState = listOf(
    ArticleUIModel("Title5", "Image5", "Description5", "2023-01-02", "Source5"),
    ArticleUIModel("Title6", "Image6", "Description6", "2023-01-02", "Source6"),
    ArticleUIModel("Title7", "Image7", "Description7", "2023-01-02", "Source7"),
)

val insert_dummySuccess_HomeState = listOf(
    ArticleUIModel("Title1", "Image1", "Description1", "2023-01-01", "Source1"),
    ArticleUIModel("Title2", "Image2", "Description2", "2023-01-02", "Source2"),
    ArticleUIModel("Title3", "Image3", "Description3", "2023-01-02", "Source3"),
    ArticleUIModel("Title4", "Image4", "Description4", "2023-01-02", "Source4"),
    ArticleUIModel("Title5", "Image5", "Description5", "2023-01-02", "Source5"),
    ArticleUIModel("Title6", "Image6", "Description6", "2023-01-02", "Source6"),
    ArticleUIModel("Title7", "Image7", "Description7", "2023-01-02", "Source7"),
)