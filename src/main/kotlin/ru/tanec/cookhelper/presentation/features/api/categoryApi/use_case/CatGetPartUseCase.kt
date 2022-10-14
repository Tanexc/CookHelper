package ru.tanec.cookhelper.presentation.features.api.categoryApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.enterprise.model.entity.Category
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.CategoryRepository

object CatGetPartUseCase {
    suspend operator fun invoke(
        repository: CategoryRepository,
        parameters: Parameters
    ): ApiResponse<List<Category>> {

        val part = parameters["part"]?.toIntOrNull()
        val div = parameters["div"]?.toIntOrNull()

        if ((part == null) or (div == null)) {
            return ApiResponse(RecipeStatus.PARAMETER_MISSED, "parameter missed", null)
        }

        val state = repository.getAll().last()

        return ApiResponse(state.status, state.message, state.data)
    }
}