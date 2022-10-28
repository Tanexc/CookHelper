package ru.tanec.cookhelper.enterprise.use_case.ingredientApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository

object IngGetByIdUseCase {
    suspend operator fun invoke(
        repository: IngredientRepository,
        parameters: Parameters
    ): ApiResponse<Ingredient> {

        val id = parameters["id"]?.toLongOrNull()
            ?: return ApiResponse(PARAMETER_MISSED, "parameter missed", null)

        val state = repository.getById(
            id
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}