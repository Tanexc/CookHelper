package ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.INVALID_TOKEN
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.constants.status.UserStatus
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.UserRepository

class CommentRecipeUseCase {
    suspend operator fun invoke(
        recipeRepository: RecipeRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Recipe> {

        var id: Long? = null
        var token: String? = null
        var text: String? = null

        parameters.forEach {
            when (it.name) {
                "id" -> {
                    if (it is PartData.FormItem) {
                        id = it.value.toLong()
                    }
                }
                "token" -> {
                    if (it is PartData.FormItem) {
                        token = it.value
                    }
                }
                "text" -> {
                    if (it is PartData.FormItem) {
                        text = it.value
                    }
                }
            }
        }

        if (id == null) return ApiResponse(RecipeStatus.PARAMETER_MISSED, MISSED, null)
        if (token == null) return ApiResponse(RecipeStatus.PARAMETER_MISSED, INVALID_TOKEN, null)

        val data = recipeRepository.getById(id!!).last().data ?: return ApiResponse(
            RecipeStatus.RECIPE_NOT_FOUND,
            "recipe not found",
            null
        )
        val user = userRepository.getByToken(token!!).last().data ?: return ApiResponse(
            UserStatus.USER_NOT_FOUND,
            INVALID_TOKEN,
            null
        )

        //TODO("comment creation + commentRepo")

        val recipe = data.copy(likes = data.likes + listOf(user.id))
        val state = recipeRepository.editRecipe(recipe).last()

        return ApiResponse(state.status, state.message, state.data)
    }

}