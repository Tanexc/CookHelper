package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.utils.FileController
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.model.entity.forum.TopicData

object CreateTopicUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Topic?> {

        val topicData = TopicData()


        parameters.forEach { param ->
            when (param) {
                is PartData.FormItem -> {
                    when (param.name) {
                        "problem" -> topicData.problem = param.value.filter { it != '"' }
                        "title" -> topicData.title = param.value.filter { it != '"' }
                        "token" -> topicData.authorId = checkUserToken(userRepository, param.value)?.id
                    }
                }

                is PartData.FileItem -> {
                    val data = FileController.uploadFile(attachmentDataFloder, param)
                    topicData.attachment = topicData.attachment + listOf(data)
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
