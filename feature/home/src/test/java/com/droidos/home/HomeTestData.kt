package com.droidos.home

import com.droidos.model.CharacterModel

val dummySuccess_FirstSetOfData =
    CharacterModel(
        id = 1,
        name = "Rick Sanchez",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        status = "Alive",
        species = "Human",
    )

val dummySuccess_HomeState =
    listOf(
        CharacterModel(
            1,
            "Rick Sanchez",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        ),
        CharacterModel(
            2,
            "Morty Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        ),
        CharacterModel(
            3,
            "Summer Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        ),
        CharacterModel(
            4,
            "Beth Smith",
            "Alive",
            "Human",
            "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        ),
    )
