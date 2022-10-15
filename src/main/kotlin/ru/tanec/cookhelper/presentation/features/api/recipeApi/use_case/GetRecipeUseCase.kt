package ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository

object GetRecipeUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        parameters: Parameters
    ): ApiResponse<Recipe> {

        val state = when(val id = parameters["id"]) {
            null -> State.Error(status=RecipeStatus.PARAMETER_MISSED)
            else -> repository.getById(
                id.toLong()
            ).last()
        }

        return ApiResponse(state.status, state.message, state.data)
    }
}
