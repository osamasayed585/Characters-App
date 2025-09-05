package com.yassir.data.remote

import com.yassir.common.utils.Constants.INITIAL_PAGE
import com.yassir.common.utils.Constants.PAGE_SIZE
import com.yassir.model.beans.CharacterDto
import com.yassir.model.response.CharactersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharactersService {

    @GET("character")
    suspend fun requestCharacters(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
    ): Response<CharactersResponse>

    @GET("character")
    suspend fun searchCharacters(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
        @Query("name") name: String
    ): Response<CharactersResponse>

    @GET("character/{id}")
    suspend fun requestCharacterDetail(@Path("id") id: Int): Response<CharacterDto>
}