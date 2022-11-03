package ru.tanec.cookhelper.enterprise.use_case.userApi


import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository


object RegistrationUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<User> {

        val paramData = parameters.fromMultipart() ?: return ApiResponse(104, MISSED, null)


        val state = repository.register(
            paramData.asDomain()
        ).last()

        return ApiResponse(state.status, state.message, state.data?.privateInfo())
    }

    private fun List<PartData>.fromMultipart(): RegistrationData? {
        val r = RegistrationData()
        var _params = 5
        this.forEach {
            if (it is PartData.FormItem) {
                when (it.name) {
                    "name" -> {
                        r.name = it.value.filter {it != '"'}
                        _params -= 1
                    }
                    "surname" -> {
                        r.surname = it.value.filter {it != '"'}
                        _params -= 1
                    }
                    "nickname" -> {
                        r.nickname = it.value.filter {it != '"'}
                        _params -= 1
                    }
                    "email" -> {
                        r.email = it.value.filter {it != '"'}
                        _params -= 1
                    }
                    "password" -> {
                        r.password = it.value.filter {it != '"'}
                        _params -= 1
                    }
                }
            }
        }
        return if (_params == 0) r else null
    }
}