package ru.tanec.cookhelper.enterprise.model.entity.recipe

import io.ktor.util.date.*
import kotlinx.serialization.Serializable

@Serializable
data class Recipe(
    val id: Long = 0,

    val authorId: Long?,
    val title: String,
    val cookSteps: List<String>,
    val time: Long,
    val ingredients: List<Long>,
    val category: Long,

    val proteins: Double,
    val carbohydrates: Double,
    val fats: Double,
    val calories: Double,

    val image: String,
    val comments: List<Long> = listOf(),
    val reposts: List<Long> = listOf(),
    val likes: List<Long> = listOf(),
    val timestamp: Long = getTimeMillis()
)
