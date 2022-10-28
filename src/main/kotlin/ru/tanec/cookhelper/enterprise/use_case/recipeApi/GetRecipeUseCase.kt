package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository

object GetRecipeUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        parameters: Parameters
    ): ApiResponse<Recipe> {

        val state = when(val id = parameters["id"]) {
            null -> State.Error(status=PARAMETER_MISSED)
            else -> repository.getById(
                id.toLong()
            ).last()
        }

        return ApiResponse(state.status, state.message, state.data)
    }
}
