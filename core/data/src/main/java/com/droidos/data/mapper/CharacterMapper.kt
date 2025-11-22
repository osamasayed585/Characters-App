package com.droidos.data.mapper

import com.droidos.model.beans.CharacterDto
import com.droidos.model.beans.CharacterUIModel

fun CharacterDto.asExternalUiModel() =
    CharacterUIModel(
        id = id,
        name = name,
        image = image,
        species = species,
        status = status,
    )
