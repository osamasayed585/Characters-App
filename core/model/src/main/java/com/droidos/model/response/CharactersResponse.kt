package com.droidos.model.response

import com.droidos.model.beans.CharacterDto
import com.droidos.model.beans.PageInfo

data class CharactersResponse(
    val results: List<CharacterDto>,
    val info: PageInfo,
)