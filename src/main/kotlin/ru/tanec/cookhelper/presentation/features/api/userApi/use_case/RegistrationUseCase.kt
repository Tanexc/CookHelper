package ru.tanec.cookhelper.presentation.features.api.userApi.use_case


import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.utils.allNotNull
import ru.tanec.cookhelper.enterprise.model.entity_data.User
import ru.tanec.cookhelper.enterprise.model.receive_data.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response_data.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.UserRepository


object RegistrationUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: RegistrationData
    ): ApiResponse<User> {
        if (!allNotNull(
                listOf(
                    parameters.name,
                    parameters.surname,
                    parameters.nickname,
                    parameters.email,
                    parameters.password
                )
            )
        ) return ApiResponse(104, MISSED, null)


        val state = repository.register(
            parameters.name?: "",
            parameters.surname?: "",
            parameters.nickname?: "",
            parameters.email?: "",
            parameters.password?: ""
        ).last()

        return ApiResponse(state.status, state.message, state.data)
    }
}