package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.database.utils.FileController
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.database.utils.FileController.uploadFile
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.model.entity.forum.TopicData
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object CreateTopicUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<TopicResponseData?> {

        try {
            val topicData = TopicData()

            var token: String? = null

            parameters.filterIsInstance<PartData.FormItem>().forEach { parameter ->
                when (parameter.name) {
                    "token" -> token = parameter.value
                    "title" -> topicData.title = parameter.value
                    "text" -> topicData.text = parameter.value
                    "tags" -> topicData.tags = parameter.value.split("*").filter { it.isNotEmpty() }
                }
            }

            if (token == null) return State.Error<TopicResponseData?>(
                status = PARAMETER_MISSED,
                message = REQUIRED("token")
            ).asApiResponse()

            if (!topicData.equipped()) return State.Error<TopicResponseData?>(
                status = PARAMETER_MISSED,
                message = REQUIRED("title, text, tags")
            ).asApiResponse()

            val userState = userRepository.getByToken(token ?: "").last()

            var user = userState.data ?: return State.Error<TopicResponseData?>(status = USER_NOT_FOUND).asApiResponse()

            topicData.authorId = user.id

            parameters.filterIsInstance<PartData.FileItem>().forEach {
                topicData.attachment = topicData.attachment.plus(
                    uploadFile(
                        attachmentDataFloder,
                        it,
                        "${it.contentType?.contentType}/${it.contentType?.contentSubtype}"
                    )
                )
            }


            val state = if (topicData.equipped()) repository.insert(topicData.asDomain()).last() else State.Error(
                data = null,
                status = PARAMETER_MISSED
            )

            user = user.copy(topics = user.topics.plus(state.data?.id).filterNotNull())

            userWebsocketConnectionController.updateData(user, userRepository)

            return ApiResponse(state.status, state.message, state.data?.asResponseData(user.smallInfo(), emptyList()))

        } catch (e: Exception) {
            return State.Error<TopicResponseData?>(status = EXCEPTION, message = "error in CreateTopicUseCase").asApiResponse()
        }
    }
}
