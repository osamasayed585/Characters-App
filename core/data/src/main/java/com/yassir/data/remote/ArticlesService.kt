package com.yassir.data.remote

import com.yassir.common.utils.Constants.INITIAL_PAGE
import com.yassir.common.utils.Constants.PAGE_SIZE
import com.yassir.model.beans.CharacterDto
import com.yassir.model.response.CharactersResponse
import com.yassir.network.di.errorHandler.BaseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ArticlesService {

    @GET("character")
    suspend fun requestCharacters(
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
    ): Response<BaseResponse<List<CharacterDto>>>

    @GET("character")
    suspend fun requestCharacterDetail(
        @Query("query") query: String,
        @Query("pageSize") pageSize: Int = PAGE_SIZE,
        @Query("page") page: Int = INITIAL_PAGE,
    ): Response<BaseResponse<CharacterDto>>
}