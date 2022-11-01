package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket.ChatReceiveMessageData
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController

fun chatWebsocketRoutes(
    route: Routing,
    controller: ChatConnectionController
) {

    route.webSocket("/websocket/chat/") {

        val flow = controller.connect(this, call.parameters)
        var user: User?


        flow.collect {
            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    try {
                        for (frame in incoming) {

                            frame as? Frame.Text?: continue

                            val data: ChatReceiveMessageData = this.receiveDeserialized()

                            when (val messageData = controller.receiveMessage(data, user!!)) {
                                is State.Success -> {
                                    controller.sendMessage(
                                        message = messageData.data!!,
                                        user = user!!,
                                        chatId = it.data!!.id
                                    )
                                }

                                else -> break
                            }

                        }
                    } finally {
                        controller.disconnect(this, it.data!!.id)
                        this.close()
                    }
                }
            }
        }
    }
}