package ru.tanec.cookhelper.presentation.features.api.userApi.use_case

import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.userApi.LoginData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.UserRepository

object LoginUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: LoginData
    ): ApiResponse<User> {



        val state = repository.login(
            parameters.login ?: "",
            parameters.password ?: ""
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}