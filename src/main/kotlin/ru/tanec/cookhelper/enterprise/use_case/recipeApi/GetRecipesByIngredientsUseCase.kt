package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetRecipesByIngredientsUseCase {
    suspend operator fun invoke(
        repository: RecipeRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<List<Recipe>?> {
        val limit = parameters["limit"]?.toIntOrNull()?: 40

        val offset = parameters["offset"]?.toIntOrNull()?: 0

        val ids = parameters["listId"]?.split("*")?.mapNotNull { it.toLongOrNull() } ?: return ApiResponse(
            status = PARAMETER_MISSED,
            message = "error",
            data = null
        )
        val token = parameters["token"]?: return ApiResponse(
            status = PARAMETER_MISSED,
            message = "error",
            data = null
        )

        val user = checkUserToken(userRepository, token)?: return ApiResponse(
            status = USER_TOKEN_INVALID,
            message = "error",
            data = null
        )

        userWebsocketConnectionController.updateData(user, userRepository)

        return repository.getRecipeByIngredients(ids, offset, limit).last().asApiResponse()
    }
}