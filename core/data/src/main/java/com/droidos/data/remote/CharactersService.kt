package com.droidos.data.remote

import com.droidos.common.utils.Constants.INITIAL_PAGE
import com.droidos.common.utils.Constants.PAGE_SIZE
import com.droidos.model.beans.CharacterDto
import com.droidos.model.response.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("character")
    suspend fun fetchCharacters(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
    ): Response<CharactersResponse>

    @GET("character")
    suspend fun searchCharacters(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
        @Query("name") name: String
    ): CharactersResponse

    @GET("character/{id}")
    suspend fun fetchCharacterDetail(@Path("id") id: Int): Response<CharacterDto>
}