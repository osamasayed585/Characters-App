package com.yassir.model.response

import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.PageInfo

data class CharactersResponse(
    val results: List<CharacterDto>,
    val info: PageInfo,
)