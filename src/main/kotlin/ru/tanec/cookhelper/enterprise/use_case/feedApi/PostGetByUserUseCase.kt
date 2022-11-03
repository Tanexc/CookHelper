package ru.tanec.cookhelper.enterprise.use_case.feedApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.INVALID_TOKEN
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object PostGetByUserUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<List<Post>> {
        when(val token = parameters["token"]) {
            null -> return ApiResponse(PARAMETER_MISSED, MISSED, null)
            else -> {
                val user = checkUserToken(userRepository, token)?: return ApiResponse(USER_TOKEN_INVALID, INVALID_TOKEN, null)

                userWebsocketConnectionController.updateData(user, userRepository)

                val offset = parameters["offset"]?.toIntOrNull()
                val limit = parameters["limit"]?.toIntOrNull()
                val state = repository.getByUser(user.id, offset, limit).last()
                return ApiResponse(state.status, state.message, state.data)
            }
        }

    }
}