package ru.tanec.cookhelper.presentation.features.api.ingredientApi.use_case

import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository

object IngGetAllUseCase {
    suspend operator fun invoke(
        repository: IngredientRepository
    ): ApiResponse<List<Ingredient>> {

        val state = repository.getAll().last()

        return ApiResponse(state.status, state.message, state.data)
    }
}