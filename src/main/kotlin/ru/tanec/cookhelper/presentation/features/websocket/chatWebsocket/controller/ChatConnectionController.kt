package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.*
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.database.repository.ChatConnectionRepository

class ChatConnectionController(
    val repository: ChatConnectionRepository
) {
    fun connect(
        session: DefaultWebSocketServerSession,
        parameters: Parameters
    ): State<out Any> {

        val chatId = 0L

        return if (chatId == null) {
            State.Error(message=REQUIRED("id"), status=399)
        } else {
            repository.add(chatId, session)
            State.Success(status=300)
        }

    }

    suspend fun sendMessage(
        session: DefaultWebSocketServerSession,
        chatId: Long?,
        message: Message
    ): State<out Any> {
        when(val receivers = chatId?.let { repository.get(it, session) }) {
            is State.Success -> {

                for (r: DefaultWebSocketServerSession in receivers.data!!) {
                    r.sendSerialized(message)
                }

                return State.Success(status=300)

            }

            else -> {return State.Error(message="some error occurred", status = 399)}
        }

    }

    fun disconnect(
        session: DefaultWebSocketServerSession,
        chatId: Long?
    ): State<out Any> {
        return if (chatId == null) {
            State.Error(message=REQUIRED("id"), status = 399)
        } else {
            repository.del(chatId, session)
            State.Success(status=305)
        }
    }

    fun connected(
        session: DefaultWebSocketServerSession,
        chatId: Long
    ): Boolean {
        return repository.get(chatId, session).data?.contains(session) ?: false
    }
}