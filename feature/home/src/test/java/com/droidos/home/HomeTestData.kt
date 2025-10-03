package com.droidos.home

import com.droidos.model.beans.CharacterUIModel

val dummySuccess_FirstSetOfData = CharacterUIModel(
    id = 1,
    name = "Rick Sanchez",
    image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
    status = "Alive",
    species = "Human"
)

val dummySuccess_HomeState = listOf(
    CharacterUIModel(1, "Rick Sanchez", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    CharacterUIModel(2, "Morty Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
    CharacterUIModel(3, "Summer Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
    CharacterUIModel(4, "Beth Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
)

val remove_dummySuccess_HomeState = listOf(
    CharacterUIModel(1, "Rick Sanchez", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    CharacterUIModel(3, "Summer Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
)

val changes_dummySuccess_HomeState = listOf(
    CharacterUIModel(1, "Rick Sanchez", "Dead", "Human", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    CharacterUIModel(2, "Morty Smith", "Unknown", "Human", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
    CharacterUIModel(3, "Summer Smith", "Alive", "Alien", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
)

val insert_dummySuccess_HomeState = listOf(
    CharacterUIModel(1, "Rick Sanchez", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
    CharacterUIModel(2, "Morty Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
    CharacterUIModel(3, "Summer Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
    CharacterUIModel(4, "Beth Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
    CharacterUIModel(5, "Jerry Smith", "Alive", "Human", "https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
    CharacterUIModel(6, "Abadango Cluster Princess", "Alive", "Alien", "https://rickandmortyapi.com/api/character/avatar/6.jpeg"),
    CharacterUIModel(7, "Abradolf Lincler", "Unknown", "Human", "https://rickandmortyapi.com/api/character/avatar/7.jpeg"),
)