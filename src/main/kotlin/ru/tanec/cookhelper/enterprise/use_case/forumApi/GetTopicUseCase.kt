package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetTopicUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<List<Topic>?> {
        val token = parameters["token"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("token"), null)
        val title = parameters["title"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("title"), null)

        val user = checkUserToken(userRepository, token) ?: return ApiResponse(USER_TOKEN_INVALID, "invalid token", null)

        userRepository.action(user)

        userWebsocketConnectionController.updateData(user, userRepository)

        val state = repository.getByTitle(title).last()
        return state.asApiResponse()

    }
}