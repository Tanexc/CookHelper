package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.recipeDataFolder
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.database.utils.FileController.uploadFile
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.receive.recipeApi.RecipeData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController


object RecipeCreateUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<Recipe?> {

        val state = when (val recipe = fromMultipart(parameters, userRepository, userWebsocketConnectionController)?.asDomain()) {
            null -> {
                State.Error(status = PARAMETER_MISSED)
            }

            else -> {
                if (recipe.authorId != null)
                    repository.insert(
                        recipe
                    ).last()
                else State.Error(status = USER_NOT_FOUND)
            }
        }

        return state.asApiResponse()
    }


    private suspend fun fromMultipart(partList: List<PartData>, userRepository: UserRepository, userWebsocketConnectionController: UserWebsocketConnectionController): RecipeData? {
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
                            userWebsocketConnectionController.updateData(user, userRepository)
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
                    if (pt.contentType != null) {
                        image = pt
                        _params -= 1
                    }
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

    private suspend fun RecipeData.asDomain(): Recipe {
        return Recipe(
            authorId = this.authorId,
            calories = this.calories,
            cookSteps = this.cookSteps,
            fats = this.fats,
            carbohydrates = this.carbohydrates,
            time = this.time,
            title = this.title,
            image = uploadFile(recipeDataFolder, this.image, this.image.contentType!!.contentType),
            category = this.category,
            ingredients = this.ingredients,
            proteins = this.proteins
        )
    }
}
