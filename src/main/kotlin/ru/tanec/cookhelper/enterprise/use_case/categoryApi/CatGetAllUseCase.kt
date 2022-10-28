package ru.tanec.cookhelper.enterprise.use_case.categoryApi

import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Category
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CategoryRepository

object CatGetAllUseCase {
    suspend operator fun invoke(
        repository: CategoryRepository
    ): ApiResponse<List<Category>> {

        val state = repository.getAll().last()

        return ApiResponse(state.status, state.message, state.data)
    }
}