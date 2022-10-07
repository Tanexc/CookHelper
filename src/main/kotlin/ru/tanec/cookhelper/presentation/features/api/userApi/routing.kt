package ru.tanec.cookhelper.presentation.features.api.userApi

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.*
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.db.repository.UserRepositoryImpl
import ru.tanec.cookhelper.enterprise.model.entity_data.User
import ru.tanec.cookhelper.enterprise.model.receive_data.userApi.LoginData
import ru.tanec.cookhelper.enterprise.model.receive_data.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response_data.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.UserRepository
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.LoginUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.RegistrationUseCase

fun userApiRoutes(
    route: Routing,
    repository: UserRepository) {

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
        println(params["login"])
        call.respond(LoginUseCase(repository, LoginData(params["login"], params["password"])))
    }
}
