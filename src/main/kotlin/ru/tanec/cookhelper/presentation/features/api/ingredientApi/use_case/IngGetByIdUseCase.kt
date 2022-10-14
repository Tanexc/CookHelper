package ru.tanec.cookhelper.presentation.features.api.ingredientApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.enterprise.model.entity.Ingredient
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.IngredientRepository

object IngGetByIdUseCase {
    suspend operator fun invoke(
        repository: IngredientRepository,
        parameters: Parameters
    ): ApiResponse<Ingredient> {

        val id = parameters["id"]?.toLongOrNull()
            ?: return ApiResponse(RecipeStatus.PARAMETER_MISSED, "parameter missed", null)

        val state = repository.getById(
            id
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}