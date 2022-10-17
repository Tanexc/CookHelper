package ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.constants.status.UserStatus
import ru.tanec.cookhelper.core.utils.FileController.uploadRecipeImage
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.receive.recipeApi.RecipeData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository


object RecipeCreateUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Recipe?> {

        val state = when (val recipe = fromMultipart(parameters, userRepository)?.asDomain()) {
            null -> {
                State.Error(status = RecipeStatus.PARAMETER_MISSED)
            }

            else -> {
                if (recipe.authorId != null)
                repository.insert(
                    recipe
                ).last()
                else State.Error(status=UserStatus.USER_NOT_FOUND)
            }
        }

        return state.asApiResponse()
    }


    private suspend fun fromMultipart(partList: List<PartData>, userRepository: UserRepository): RecipeData? {
        var title: String? = null
        var authorId: Long? = null
        var cookSteps: List<String>? = null
        var ingredients: List<Long>? = null
        var category: Long? = null
        var time: Long? = null
        var image: PartData.FileItem? = null

        var proteins: Double? = null
        var carbohydrates: Double? = null
        var fats: Double? = null
        var calories: Double? = null

        var _params = 10

        partList.forEach { pt ->
            if (pt is PartData.FormItem)
                when (pt.name) {
                    "title" -> {
                        title = pt.value
                        _params -= 1
                    }

                    "cookSteps" -> {
                        cookSteps = pt.value.split("\n")
                        _params -= 1
                    }

                    "token" -> {
                        val user = userRepository.getByToken(pt.value).last().data
                        if (user != null) {
                            authorId = user.id
                        }
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
                authorId,
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
            authorId = this.authorId,
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
