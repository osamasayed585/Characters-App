package com.droidos.data.mapper

import com.droidos.model.CharacterModel
import com.droidos.model.beans.CharacterDto

fun CharacterDto.asExternalUiModel() =
    CharacterModel(
        id = id,
        name = name,
        image = image,
        species = species,
        status = status,
    )
