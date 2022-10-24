package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket.ChatReceiveMessageData
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveAnswerData
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController

suspend fun chatWebsocket(
    route: Routing,
    controller: ChatConnectionController
) {

    route.webSocket("/websocket/chat/") {

        val flow = controller.connect(this, call.parameters)
        var user: User?

        flow.collect {

            this.sendSerialized(it.asWebsocketResponse())

            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    try {
                        for (frame in incoming) {

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