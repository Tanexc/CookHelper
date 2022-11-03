package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket.ChatReceiveMessageData
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController

fun Routing.chatWebsocketRoutes() {

    val controller: ChatConnectionController by inject()

    webSocket("/websocket/chat/") {

        val flow = controller.connect(this, call.parameters)
        var user: User?

        flow.collect {
            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    try {
                        var process = true
                        while (process) {
                            val data: ChatReceiveMessageData = receiveDeserialized()
                            when (val messageData = controller.receiveMessage(data, user!!)) {
                                is State.Success -> {
                                    controller.sendMessage(
                                        message = messageData.data!!,
                                        user = user!!,
                                        chatId = it.data!!.id
                                    )
                                }

                                else -> process = false
                            }
                        }
                    } finally {
                        controller.disconnect(this, it.data?.id)
                        this.close()

                        println("AAAAAAAAAAAAAAA ${it.status}")
                    }
                }
            }
        }
    }
}