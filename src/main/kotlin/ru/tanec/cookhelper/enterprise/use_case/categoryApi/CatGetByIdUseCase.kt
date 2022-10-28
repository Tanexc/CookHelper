package ru.tanec.cookhelper.enterprise.use_case.categoryApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Category
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CategoryRepository

object CatGetByIdUseCase {
    suspend operator fun invoke(
        repository: CategoryRepository,
        parameters: Parameters
    ): ApiResponse<Category> {

        val id = parameters["id"]?.toLongOrNull()
            ?: return ApiResponse(PARAMETER_MISSED, "parameter missed", null)

        val state = repository.getById(
            id
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}