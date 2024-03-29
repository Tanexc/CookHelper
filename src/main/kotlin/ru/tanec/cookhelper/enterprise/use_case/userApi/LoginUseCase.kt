package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object LoginUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
        ): ApiResponse<User> {

            var login = ""
            var password = ""

            parameters.forEach {
                when(it.name) {
                    "login" -> login = if (it is PartData.FormItem) it.value.filter {it != '"'} else ""
                    "password" -> password = if (it is PartData.FormItem) it.value.filter {it != '"'} else ""
                }
            }

            print("$login $password")

            var state = repository.login(
                login,
                password
            ).last()

            if (state.data != null) {
                state = State.Success(
                    data=state.data?.privateInfo()
                )
            }

            state.data?.let{userWebsocketConnectionController.updateData(it, repository)}

            return ApiResponse(state.status, state.message, state.data)
        }
}