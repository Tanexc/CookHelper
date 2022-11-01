package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.descriptors.PrimitiveKind
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
                        withContext(Dispatchers.IO) {
                            incoming.receiveAsFlow().filterIsInstance<ChatReceiveMessageData>().collect { data ->
                                when (val messageData = controller.receiveMessage(data, user!!)) {
                                    is State.Success -> {
                                        println("message sent")
                                        controller.sendMessage(
                                            message = messageData.data!!,
                                            user = user!!,
                                            chatId = it.data!!.id
                                        )
                                    }

                                    else -> cancel()
                                }
                            }
                        }
                    } finally {
                        controller.disconnect(this, it.data?.id)
                        this.close()
                    }
                }
            }
        }
    }
}