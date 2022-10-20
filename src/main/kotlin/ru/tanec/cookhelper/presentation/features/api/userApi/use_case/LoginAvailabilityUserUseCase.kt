package ru.tanec.cookhelper.presentation.features.api.userApi.use_case

import io.ktor.http.*
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.UserStatus
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object LoginAvailabilityUserUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: Parameters
    ): ApiResponse<User?> {
        val login = parameters["login"]?: return ApiResponse(status = UserStatus.PARAMETER_MISSED,data=null, message= REQUIRED("login"))
        return ApiResponse(status = UserStatus.PARAMETER_MISSED, message= REQUIRED("login"), data=null)

    }
}