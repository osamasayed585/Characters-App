package com.droidos.data

import com.droidos.model.beans.CharacterDto
import com.droidos.model.beans.LocationDto
import com.droidos.model.beans.OriginDto
import com.droidos.model.beans.PageInfo
import com.droidos.model.response.CharactersResponse
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

val character1 =
    CharacterDto(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        type = "",
        gender = "Male",
        origin =
            OriginDto(
                name = "Earth (C-137)",
                url = "https://rickandmortyapi.com/api/location/1",
            ),
        location =
            LocationDto(
                name = "Hope Reilly",
                url = "http://www.bing.com/search?q=placerat",
            ),
        image = "eos",
        episode = listOf(),
        url = "https://search.yahoo.com/search?p=equidem",
        created = "voluptatibus",
    )

val charactersResponse =
    listOf(
        character1,
        CharacterDto(
            id = 2,
            name = "Morty Rich",
            status = "Alive",
            species = "Human",
            type = "",
            gender = "Male",
            origin =
                OriginDto(
                    name = "Earth (C-137)",
                    url = "https://rickandmortyapi.com/api/location/1",
                ),
            location =
                LocationDto(
                    name = "Hope Reilly",
                    url = "http://www.bing.com/search?q=placerat",
                ),
            image = "eos",
            episode = listOf(),
            url = "https://search.yahoo.com/search?p=equidem",
            created = "voluptatibus",
        ),
    )

val mockCharactersResponse =
    CharactersResponse(
        info =
            PageInfo(
                count = charactersResponse.size,
                pages = 1,
                next = null,
                prev = null,
            ),
        results = charactersResponse,
    )

/** A character that differs from every other only by id, for identity-focused assertions. */
fun characterDto(id: Int): CharacterDto = character1.copy(id = id, name = "Character $id")

/**
 * Builds a page whose [CharactersResponse.results] carry exactly [ids], in order.
 * [next] is the raw "next page" URL the API returns; `null` marks the last page.
 */
fun charactersResponseOf(
    ids: List<Int>,
    next: String? = null,
): CharactersResponse =
    CharactersResponse(
        info =
            PageInfo(
                count = ids.size,
                pages = 2,
                next = next,
                prev = null,
            ),
        results = ids.map(::characterDto),
    )

fun charactersHttpResponseOf(
    ids: List<Int>,
    next: String? = null,
): Response<CharactersResponse> = Response.success(charactersResponseOf(ids, next))

val mockResponse: Response<CharactersResponse> = Response.success(mockCharactersResponse)
val mockCharacterResponse: Response<CharacterDto> = Response.success(character1)
val errorResponse: Response<CharacterDto> = Response.error(404, "Response.error()".toResponseBody())
