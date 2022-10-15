package ru.tanec.cookhelper.enterprise.model.entity.recipe

import kotlinx.serialization.Serializable

@Serializable
data class Ingredient(
    val id: Long = 0,
    val title: String
)
