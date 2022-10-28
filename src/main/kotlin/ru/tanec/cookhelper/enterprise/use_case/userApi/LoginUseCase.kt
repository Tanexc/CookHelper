package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object LoginUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: List<PartData>
        ): ApiResponse<User> {

            var login: String = ""
            var password: String = ""

            parameters.forEach {
                when(it.name) {
                    "login" -> login = if (it is PartData.FormItem) it.value.filter {it != '"'} else ""
                    "password" -> password = if (it is PartData.FormItem) it.value.filter {it != '"'} else ""
                }
            }

            print("$login $password")

            val state = repository.login(
                login,
                password
            ).last()

            return ApiResponse(state.status, state.message, state.data)
        }
}