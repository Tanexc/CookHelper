package ru.tanec.cookhelper.enterprise.model.receive.recipeApi

import io.ktor.http.content.*
import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.core.utils.FileController.uploadRecipeImage
import ru.tanec.cookhelper.enterprise.model.entity.Recipe


data class RecipeData(
    val title: String,
    val owner: Long,
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