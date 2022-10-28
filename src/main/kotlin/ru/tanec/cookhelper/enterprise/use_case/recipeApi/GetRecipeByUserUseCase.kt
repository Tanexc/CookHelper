package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository

object GetRecipeByUserUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        parameters: Parameters
    ): ApiResponse<List<Recipe>> {

        val userId = parameters["userId"]?.toLongOrNull()
        val part = parameters["part"]?.toIntOrNull()
        val div = parameters["div"]?.toIntOrNull()

        if ((userId == null) || (part == null) || (div == null)) {
            return ApiResponse(PARAMETER_MISSED, "parameter missed", null)
        }

        val state = repository.getByUser(
            userId.toLong(),
            part,
            div
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}