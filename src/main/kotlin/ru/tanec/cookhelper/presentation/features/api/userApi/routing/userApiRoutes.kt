package ru.tanec.cookhelper.presentation.features.api.userApi.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.userApi.LoginData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.LoginUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.RegistrationUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.SetAvatarUseCase

fun userApiRoutes(
    route: Routing,
    repository: UserRepository
) {

    route.post("/api/user/post/reg/") {
        try {
            val data = call.receiveMultipart().readAllParts()
            call.respond(RegistrationUseCase(repository, data))
        } catch (e: Exception) {
            call.respond(ApiResponse<User>(104, MISSED, null))
        }

    }

    route.post("/api/user/post/auth/") {
        print("AUE")
        val params = call.receiveMultipart().readAllParts()
        print("AAAA")
        call.respond(LoginUseCase(repository, params))
    }

    route.post("/api/user/post/avatar/") {
        call.respond(SetAvatarUseCase(repository, call.receiveMultipart().readAllParts()))
    }
}
