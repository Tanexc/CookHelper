package ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.utils.FileController.uploadRecipeImage
import ru.tanec.cookhelper.enterprise.model.entity.Recipe
import ru.tanec.cookhelper.enterprise.model.receive.recipeApi.RecipeData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository


object RecipeCreateUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        parameters: List<PartData>
    ): ApiResponse<Recipe> {

        val state = when (val recipe = fromMultipart(parameters)?.asDomain()) {
            null -> {
                State.Error(status = RecipeStatus.PARAMETER_MISSED)
            }

            else -> {
                repository.insert(
                    recipe
                ).last()
            }
        }

        return ApiResponse(state.status, state.message, state.data)
    }


    private fun fromMultipart(partList: List<PartData>): RecipeData? {
        var title: String? = null
        var owner: Long? = null
        var cookSteps: List<String>? = null
        var ingredients: List<Long>? = null
        var category: Long? = null
        var time: Long? = null
        var image: PartData.FileItem? = null

        var proteins: Double? = null
        var carbohydrates: Double? = null
        var fats: Double? = null
        var calories: Double? = null

        var _params = 11

        partList.forEach { pt ->
            if (pt is PartData.FormItem)
                when (pt.name) {
                    "title" -> {
                        title = pt.value
                        _params -= 1
                    }

                    "owner" -> {
                        owner = pt.value.toLong()
                        _params -= 1
                    }

                    "cookSteps" -> {
                        cookSteps = pt.value.split("\n")
                        _params -= 1
                    }

                    "ingredients" -> {
                        ingredients = pt.value.split(" ").map { it.toLong() }
                        _params -= 1
                    }

                    "category" -> {
                        category = pt.value.toLong()
                        _params -= 1
                    }

                    "time" -> {
                        time = pt.value.toLong()
                        _params -= 1
                    }

                    "proteins" -> {
                        proteins = pt.value.toDouble()
                        _params -= 1
                    }

                    "calories" -> {
                        calories = pt.value.toDouble()
                        _params -= 1
                    }

                    "carbohydrates" -> {
                        carbohydrates = pt.value.toDouble()
                        _params -= 1
                    }

                    "fats" -> {
                        fats = pt.value.toDouble()
                        _params -= 1
                    }

                    else -> {}
                }
            else if (pt is PartData.FileItem) when (pt.name) {
                "image" -> {
                    image = pt
                    _params -= 1
                }
            }

        }

        println(category)

        return when (_params) {
            0 -> RecipeData(
                title!!,
                owner!!,
                cookSteps!!,
                ingredients!!,
                category!!,
                time!!,
                image!!,
                proteins!!,
                carbohydrates!!,
                fats!!,
                calories!!
            )

            else -> null

        }
    }

    private fun RecipeData.asDomain(): Recipe {
        return Recipe(
            owner = this.owner,
            calories = this.calories,
            cookSteps = this.cookSteps,
            fats = this.fats,
            carbohydrates = this.carbohydrates,
            time = this.time,
            title = this.title,
            image = uploadRecipeImage(this.image, this.category),
            category = this.category,
            ingredients = this.ingredients,
            proteins = this.proteins
        )
    }
}
