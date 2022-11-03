package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetTopicByIdUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<TopicResponseData?> {
        val token = parameters["token"]?.filter { it != '"' } ?: return ApiResponse(
            status = PARAMETER_MISSED,
            message = REQUIRED("token"),
            data = null
        )
        val id = parameters["id"]?.filter { it != '"' }?.toLongOrNull() ?: return ApiResponse(
            status = PARAMETER_MISSED,
            message = REQUIRED("id"),
            data = null
        )

        val user = checkUserToken(userRepository, token) ?: return ApiResponse(
            status = USER_TOKEN_INVALID,
            message = "error",
            data = null
        )

        userWebsocketConnectionController.updateData(user, userRepository)

        val topic = repository.getByListId(listOf(id)).last().data?.firstOrNull() ?: return ApiResponse(
            status = TOPIC_NOT_FOUND,
            message = "error",
            data = null
        )

        val replies = repository.getTopicReplies(topic.id, 0, 200).map {
            it.asResponseData(
                userRepository.getById(it.authorId).last().data,
                repository.getReplies(it.replies)
            )
        }

        val topicData: TopicResponseData = topic.asResponseData(userRepository.getById(topic.authorId).last().data?.smallInfo()?: user.smallInfo(), replies)

        return ApiResponse(status = SUCCESS, message = "success", topicData)
    }
}