package com.yassir.model.beans


data class CharacterUIModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val image: String
)

val testCharacters = listOf(
    CharacterUIModel(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
    ),
    CharacterUIModel(
        id = 2,
        name = "Morty Smith",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg"
    ),
    CharacterUIModel(
        id = 3,
        name = "Summer Smith",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg"
    ),
    CharacterUIModel(
        id = 4,
        name = "Beth Smith",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg"
    ),
    CharacterUIModel(
        id = 5,
        name = "Jerry Smith",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg"
    ),
    CharacterUIModel(
        id = 6,
        name = "Abadango Cluster Princess",
        status = "Alive",
        species = "Alien",
        image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg"
    ),
    CharacterUIModel(
        id = 7,
        name = "Abradolf Lincler",
        status = "unknown",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg"
    ),
    CharacterUIModel(
        id = 8,
        name = "Adjudicator Rick",
        status = "Dead",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg"
    ),
    CharacterUIModel(
        id = 9,
        name = "Agency Director",
        status = "Dead",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/9.jpeg"
    ),
    CharacterUIModel(
        id = 10,
        name = "Alan Rails",
        status = "Dead",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg"
    ),
    CharacterUIModel(
        id = 11,
        name = "Albert Einstein",
        status = "Dead",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/11.jpeg"
    ),
    CharacterUIModel(
        id = 12,
        name = "Alexander",
        status = "Dead",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/12.jpeg"
    ),
    CharacterUIModel(
        id = 13,
        name = "Alien Googah",
        status = "unknown",
        species = "Alien",
        image = "https://rickandmortyapi.com/api/character/avatar/13.jpeg"
    ),
    CharacterUIModel(
        id = 14,
        name = "Alien Morty",
        status = "unknown",
        species = "Alien",
        image = "https://rickandmortyapi.com/api/character/avatar/14.jpeg"
    ),
    CharacterUIModel(
        id = 15,
        name = "Alien Rick",
        status = "unknown",
        species = "Alien",
        image = "https://rickandmortyapi.com/api/character/avatar/15.jpeg"
    ),
    CharacterUIModel(
        id = 16,
        name = "Amish Cyborg",
        status = "Dead",
        species = "Alien",
        image = "https://rickandmortyapi.com/api/character/avatar/16.jpeg"
    ),
    CharacterUIModel(
        id = 17,
        name = "Annie",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/17.jpeg"
    ),
    CharacterUIModel(
        id = 18,
        name = "Antenna Morty",
        status = "Alive",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/18.jpeg"
    ),
    CharacterUIModel(
        id = 19,
        name = "Antenna Rick",
        status = "unknown",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/19.jpeg"
    ),
    CharacterUIModel(
        id = 20,
        name = "Ants in my Eyes Johnson",
        status = "unknown",
        species = "Human",
        image = "https://rickandmortyapi.com/api/character/avatar/20.jpeg"
    )
)