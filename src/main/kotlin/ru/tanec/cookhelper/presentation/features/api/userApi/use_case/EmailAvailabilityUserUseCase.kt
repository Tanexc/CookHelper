package ru.tanec.cookhelper.presentation.features.api.userApi.use_case

import io.ktor.http.*
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object EmailAvailabilityUserUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: Parameters
    ): ApiResponse<Boolean?> {
        val email = parameters["email"]?.filter { it != '"' } ?: return ApiResponse(
            status = PARAMETER_MISSED,
            data = null,
            message = REQUIRED("login")
        )
        return if (repository.emailAccessibility(email)) ApiResponse(status = SUCCESS, data = true, message = "success")
        else ApiResponse(status = EMAIL_REJECTED, data = false, message = "rejected")
    }
}