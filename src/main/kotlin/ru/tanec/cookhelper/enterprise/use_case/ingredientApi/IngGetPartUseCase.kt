package ru.tanec.cookhelper.enterprise.use_case.ingredientApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository

object IngGetPartUseCase {
    suspend operator fun invoke(
        repository: IngredientRepository,
        parameters: Parameters
    ): ApiResponse<List<Ingredient>> {

        val part = parameters["part"]?.toIntOrNull()
        val div = parameters["div"]?.toIntOrNull()

        if ((part == null) or (div == null)) {
            return ApiResponse(PARAMETER_MISSED, REQUIRED("part: Int; div: Int"), null)
        }

        val state = repository.getAll().last()

        return ApiResponse(state.status, state.message, state.data)
    }
}