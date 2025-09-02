package com.yassir.data.mapper

import com.yassir.model.beans.CharacterDto
import com.yassir.model.beans.CharacterUIModel

fun CharacterDto.asExternalUiModel() = CharacterUIModel(
    id = id,
    name = name,
    image = image,
    species = species,
    status = status
)