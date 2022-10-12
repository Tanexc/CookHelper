package ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.db.model.Recipes
import ru.tanec.cookhelper.enterprise.model.entity.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository

object GetRecipeByUserUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        parameters: Parameters
    ): ApiResponse<List<Recipe>> {

        val userId = parameters["userId"]?.toLongOrNull()
        val part = parameters["part"]?.toIntOrNull()
        val div = parameters["div"]?.toIntOrNull()

        if ((userId == null) || (part == null) || (div == null)) {
            return ApiResponse(RecipeStatus.PARAMETER_MISSED, "user id required", null)
        }

        val state = repository.getByUser(
            userId.toLong(),
            part,
            div
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}