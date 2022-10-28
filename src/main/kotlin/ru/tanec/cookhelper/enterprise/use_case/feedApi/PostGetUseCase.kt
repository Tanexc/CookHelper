package ru.tanec.cookhelper.enterprise.use_case.feedApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.PostStatus
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository

object PostGetUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        parameters: Parameters
    ): ApiResponse<List<Post>> {
        return when(val id = parameters["listId"]?.split("*")?.mapNotNull { it.toLongOrNull() }) {
            null -> ApiResponse(PostStatus.PARAMETER_MISSED, MISSED, null)
            else -> {
                val part = parameters["part"]?.toIntOrNull()
                val div = parameters["div"]?.toIntOrNull()
                val state = repository.getByList(id, part, div).last()
                ApiResponse(state.status, state.message, state.data)
            }
        }

    }
}