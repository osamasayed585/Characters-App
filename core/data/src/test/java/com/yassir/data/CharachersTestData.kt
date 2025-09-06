package com.yassir.data

import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.LocationDto
import com.yassir.model.beans.OriginDto
import com.yassir.model.beans.PageInfo
import com.yassir.model.response.CharactersResponse
import retrofit2.Response


val charactersResponse = listOf(
    CharacterDto(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = OriginDto(
            name = "Earth (C-137)",
            url = "https://rickandmortyapi.com/api/location/1"
        ),
        location = LocationDto(
            name = "Hope Reilly",
            url = "http://www.bing.com/search?q=placerat"
        ),
        image = "eos",
        episode = listOf(),
        url = "https://search.yahoo.com/search?p=equidem",
        created = "voluptatibus"
    ),
    CharacterDto(
        id = 2,
        name = "Morty Rich",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin = OriginDto(
            name = "Earth (C-137)",
            url = "https://rickandmortyapi.com/api/location/1"
        ),
        location = LocationDto(
            name = "Hope Reilly",
            url = "http://www.bing.com/search?q=placerat"
        ),
        image = "eos",
        episode = listOf(),
        url = "https://search.yahoo.com/search?p=equidem",
        created = "voluptatibus"
    ),

    )

// Create a CharactersResponse containing your mock data
val mockCharactersResponse = CharactersResponse(
    info = PageInfo(
        count = charactersResponse.size,
        pages = 1,
        next = null,
        prev = null
    ),
    results = charactersResponse
)

// Create a successful Response object
val mockResponse: Response<CharactersResponse> = Response.success(mockCharactersResponse)