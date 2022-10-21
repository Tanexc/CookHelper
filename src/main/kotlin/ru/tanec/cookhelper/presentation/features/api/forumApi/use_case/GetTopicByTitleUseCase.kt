package ru.tanec.cookhelper.presentation.features.api.forumApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.INVALID_TOKEN
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object GetTopicByTitleUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        parameters: Parameters
    ): ApiResponse<List<Topic>?> {
        val token = parameters["token"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("token"), null)
        val title = parameters["title"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("title"), null)

        val user = checkUserToken(userRepository, token)?: return ApiResponse(USER_TOKEN_INVALID, "invalid token", null)

        userRepository.action(user)

        val state = repository.getByTitle(title).last()
        return state.asApiResponse()

    }
}