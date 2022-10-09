package ru.tanec.cookhelper.presentation.features.api.userApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.utils.FileController
import ru.tanec.cookhelper.enterprise.model.entity.User
import ru.tanec.cookhelper.enterprise.model.receive.userApi.ApiRequestData
import ru.tanec.cookhelper.enterprise.model.receive.userApi.LoginData
import ru.tanec.cookhelper.enterprise.model.receive.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.UserRepository
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.LoginUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.RegistrationUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.SetAvatarUseCase

fun userApiRoutes(
    route: Routing,
    repository: UserRepository
) {

    route.post("/api/user/post/reg") {
        try {
            val data = call.receive<RegistrationData>()
            call.respond(RegistrationUseCase(repository, data))
        } catch (e: Exception) {
            call.respond(ApiResponse<User>(104, MISSED, null))
        }

    }

    route.get("/api/user/get/auth") {
        val params = call.request.queryParameters
        call.respond(LoginUseCase(repository, LoginData(params["login"], params["password"])))
    }

    route.post("/api/user/post/avatar") {
        call.respond(SetAvatarUseCase(repository, call.receiveMultipart().readAllParts()))
    }
}
