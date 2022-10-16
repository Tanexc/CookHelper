package ru.tanec.cookhelper.enterprise.model.receive.recipeApi

import io.ktor.http.content.*


data class RecipeData(
    val title: String,
    val authorId: Long?,
    val cookSteps: List<String>,
    val ingredients: List<Long>,
    val category: Long,
    val time: Long,
    val image: PartData.FileItem,

    val proteins: Double,
    val carbohydrates: Double,
    val fats: Double,
    val calories: Double
)