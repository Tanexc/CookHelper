package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.database.utils.FileController
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.model.entity.forum.TopicData
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object CreateTopicUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<Topic?> {

        val topicData = TopicData()


        parameters.forEach { param ->
            when (param) {
                is PartData.FormItem -> {
                    when (param.name) {
                        "problem" -> topicData.problem = param.value.filter { it != '"' }
                        "title" -> topicData.title = param.value.filter { it != '"' }
                        "token" -> {
                            val user = checkUserToken(userRepository, param.value)
                            user?.let {userWebsocketConnectionController.updateData(user, userRepository)}
                            topicData.authorId = user?.id}
                    }
                }

                is PartData.FileItem -> {
                    if (param.contentType != null) {
                        val data = FileController.uploadFile(attachmentDataFloder, param, param.contentType!!.contentType)
                        topicData.attachment = topicData.attachment + listOf(data)
                    }
                }

                else -> {}
            }
        }
        val state = if (topicData.equipped()) repository.insert(topicData.asDomain()).last() else State.Error(
            data = null,
            status = PARAMETER_MISSED
        )

        return ApiResponse(state.status, state.message, state.data)
    }
}
