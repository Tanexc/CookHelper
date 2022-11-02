package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object GetFridgeUseCase {
    suspend operator fun invoke(
        userRepository: UserRepository,
        ingredientRepository: IngredientRepository,
        parameters: Parameters
    ): ApiResponse<List<Ingredient>?> {
        try {
            val token: String = parameters["token"] ?: return ApiResponse(
                status = PARAMETER_MISSED,
                data = null,
                message = "error"
            )

            val user: User = checkUserToken(userRepository, token) ?: return ApiResponse(
                status = USER_TOKEN_INVALID,
                data = null,
                message = "error"
            )

            val offset = parameters["offset"]?.toIntOrNull()?: return ApiResponse(
                status = PARAMETER_MISSED,
                data = null,
                message = "error"
            )

            val limit = parameters["limit"]?.toIntOrNull()?: return ApiResponse(
                status = PARAMETER_MISSED,
                data = null,
                message = "error"
            )

            return ingredientRepository.getByListId(user.fridge, offset, limit).last().asApiResponse()

        } catch (e: Exception) {
            return ApiResponse(data = null, status = EXCEPTION, message = e.message ?: "error")
        }

    }
}