package com.yassir.model.response

import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.PageInfo

data class CharactersResponse(
    val info: PageInfo,
    val results: List<CharacterDto>
)