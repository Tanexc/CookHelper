package ru.tanec.cookhelper.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.chatWebsocket

fun Application.routes() {
    routing {

        static("/static") {
            resources("static")
        }

        webSocket("/messenger/{token}") {
            val token = call.parameters["token"]
        }

        webSocket("/w") {
            send("sdvsdvsdv")
        }

        webSocket("/chat/{id}") {
            val manager: ChatConnectionController by inject()
            chatWebsocket(
                this,
                incoming,
                manager,
                call.request.queryParameters["token"],
                call.parameters["id"]?.toLong()
            )
        }
    }
}