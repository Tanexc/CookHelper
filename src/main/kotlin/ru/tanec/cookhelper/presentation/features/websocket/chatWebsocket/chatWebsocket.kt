package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.receive_data.ChatReceiveData
import ru.tanec.cookhelper.enterprise.model.entity_data.Message
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController

suspend fun chatWebsocket(
    session: DefaultWebSocketServerSession,
    incoming: ReceiveChannel<Frame>,
    controller: ChatConnectionController,
    token: String? = null,
    chatId: Long? = null
) {

    //TODO: get user from token
    //  add messages to db

    if (controller.connect(session, chatId) is State.Success) {
        while (chatId?.let { controller.connected(session, it) } == true) {
            val message = session.receiveDeserialized<ChatReceiveData>()
            controller.sendMessage(
                session,
                chatId,
                Message(
                    1,
                    1,
                    message.text,
                    mutableListOf(),
                    123123123L
                )
            )
        }
    }
}