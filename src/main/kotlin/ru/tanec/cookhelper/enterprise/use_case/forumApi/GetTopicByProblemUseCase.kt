package ru.tanec.cookhelper.enterprise.use_case.forumApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object GetTopicByProblemUseCase {
    suspend operator fun invoke(
        repository: TopicRepository,
        userRepository: UserRepository,
        parameters: Parameters
    ): ApiResponse<List<Topic>?> {
        val token = parameters["token"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("token"), null)
        val problem = parameters["problem"]?: return ApiResponse(PARAMETER_MISSED, REQUIRED("problem"), null)

        val user = checkUserToken(userRepository, token) ?: return ApiResponse(USER_TOKEN_INVALID, "invalid token", null)

        userRepository.action(user)

        val state = repository.getByProblem(problem).last()
        return state.asApiResponse()
    }
}