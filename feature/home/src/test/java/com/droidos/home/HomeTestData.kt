package com.droidos.home

import com.droidos.model.beans.CharacterUIModel

val dummySuccess_FirstSetOfData =
    CharacterUIModel(
        id = 1,
        name = "Rick Sanchez",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        status = "Alive",
        species = "Human",
    )

val dummySuccess_HomeState =
    listOf(
        CharacterUIModel(
            1,
            "Rick Sanchez",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        ),
        CharacterUIModel(
            2,
            "Morty Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        ),
        CharacterUIModel(
            3,
            "Summer Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        ),
        CharacterUIModel(
            4,
            "Beth Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        ),
    )
