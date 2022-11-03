package ru.tanec.cookhelper.presentation.features.websocket.userWebsocket

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

fun Routing.userWebsocketRoutes() {
    val controller: UserWebsocketConnectionController by inject()
    val userRepository: UserRepository by inject()

    webSocket("websocket/user/") {
        try {
            controller.connect(call.request.queryParameters, userRepository).collect { state ->
                when (state) {
                    is State.Success -> state.data?.second?.collect {
                        println("PADLA USER $it")
                        controller.sendMessage(
                            this, WebsocketResponse(
                                data = if (state.data.first) it.privateInfo() else it.commonInfo(),
                                status = state.status
                            )
                        )
                    }

                    else -> {
                        controller.sendMessage(
                            this, WebsocketResponse(
                                data = null,
                                status = state.status
                            )
                        )
                    }
                }
            }
        }catch (_: Exception) {
            this.close()
        }

    }
}