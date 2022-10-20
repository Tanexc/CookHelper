package ru.tanec.cookhelper.presentation.features.api.userApi.use_case

import io.ktor.http.*
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.EMAIL_REJECTED
import ru.tanec.cookhelper.core.constants.status.NICKNAME_REJECTED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object NicknameAvailabilityUserUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: Parameters
    ): ApiResponse<Boolean?> {
        val nickname = parameters["nickname"]?.filter { it != '"' } ?: return ApiResponse(
            status = PARAMETER_MISSED,
            data = null,
            message = REQUIRED("nickname")
        )
        return if (repository.nicknameAccessibility(nickname)) ApiResponse(status = SUCCESS, data = true, message = "success")
        else ApiResponse(status = NICKNAME_REJECTED, data = false, message = "rejected")
    }
}