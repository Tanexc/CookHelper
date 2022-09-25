package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository

import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.domain.model.ChatReceiveObject
import ru.tanec.cookhelper.domain.model.Message
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.manager.ChatConnectionManager

suspend fun chatWebsocket(
    session: DefaultWebSocketServerSession,
    incoming: ReceiveChannel<Frame>,
    manager: ChatConnectionManager,
    token: String? = null,
    chatId: Long? = null
) {

    //TODO: get user from token
    //  add messages to db

    if (manager.connect(session, chatId) is State.Success) {
        while (chatId?.let { manager.connected(session, it) } == true) {
            val message = session.receiveDeserialized<ChatReceiveObject>()
            manager.sendMessage(
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