package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetTopicUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<List<TopicResponseData>?> {
        val token = parameters["token"] ?: return ApiResponse(PARAMETER_MISSED, REQUIRED("token"), null)
        val queryString = parameters["queryString"]?.lowercase() ?: return ApiResponse(
            PARAMETER_MISSED,
            REQUIRED("queryString"),
            null
        )

        val noRepliesFilter = parameters["noRepliesFilter"]?.toBoolean() ?: false
        val tagFilter = parameters["tagFilter"]?.split("*") ?: listOf()
        val imageFilter = parameters["imageFilter"]?.toBoolean() ?: false
        val ratingNeutralFilter = parameters["ratingNeutralFilter"]?.toBoolean() ?: false

        val ratingPositiveFilter = parameters["ratingPositiveFilter"]?.toBoolean() ?: false
        val ratingNegativeFilter = parameters["ratingNegativeFilter"]?.toBoolean() ?: false

        val ratingSort = parameters["ratingSort"]?.toBoolean() ?: false
        val reverseSort = parameters["reverseSort"]?.toBoolean() ?: false
        val recencySort = parameters["recencySort"]?.toBoolean() ?: false

        val offset = parameters["offset"]?.toIntOrNull()
        val limit = parameters["limit"]?.toIntOrNull()

        val user =
            checkUserToken(userRepository, token) ?: return ApiResponse(USER_TOKEN_INVALID, "invalid token", null)

        userRepository.action(user)

        userWebsocketConnectionController.updateData(user, userRepository)

        val state = repository.getTopicList(
            queryString,
            noRepliesFilter,
            tagFilter,
            imageFilter,
            ratingNeutralFilter,
            ratingSort,
            reverseSort,
            ratingPositiveFilter,
            ratingNegativeFilter,
            recencySort,
            offset,
            limit
        ).last()

        val data: List<TopicResponseData>? = null

        return if (state is State.Success) {
            State.Success(state.data?.mapNotNull { it.asResponseData(userRepository.getById(it.authorId).last().data?.smallInfo(), repository.getTopicReplies(it.id, 0, 200).mapNotNull { reply -> reply.asResponseData(userRepository.getById(reply.authorId).last().data, repository.getReplies(reply.replies)) }) }).asApiResponse()
        } else {
            State.Error(data=data).asApiResponse()
        }

    }
}