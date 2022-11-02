package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository

object GetTopicByListId {
    suspend operator fun invoke(
        repository: TopicRepository,
        parameters: Parameters
    ): ApiResponse<List<Topic>?> {
        val listId = parameters["listId"]?.split("*")?.mapNotNull { it.toLongOrNull() }?: return ApiResponse(PARAMETER_MISSED, REQUIRED("listId"), null)
        val state = repository.getByListId(listId).last()
        return state.asApiResponse()
    }
}