package ru.tanec.cookhelper.presentation.features.api.feedApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.PostStatus
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository

object PostGetByIdUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        parameters: Parameters
    ): ApiResponse<Post> {
        return when(val id = parameters["id"]) {
            null -> ApiResponse(PostStatus.PARAMETER_MISSED, MISSED, null)
            else -> {
                val state = repository.getById(id.toLong()).last()
                ApiResponse(state.status, state.message, state.data)
            }
        }

    }
}