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

object PostGetByUserUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        userRepository: UserRepository,
        parameters: Parameters
    ): ApiResponse<List<Post>> {
        when(val token = parameters["token"]) {
            null -> return ApiResponse(PARAMETER_MISSED, MISSED, null)
            else -> {
                val user = checkUserToken(userRepository, token)?: return ApiResponse(USER_TOKEN_INVALID, INVALID_TOKEN, null)
                val part = parameters["part"]?.toIntOrNull()
                val div = parameters["div"]?.toIntOrNull()
                val state = repository.getByUser(user.id, part, div).last()
                return ApiResponse(state.status, state.message, state.data)
            }
        }

    }
}