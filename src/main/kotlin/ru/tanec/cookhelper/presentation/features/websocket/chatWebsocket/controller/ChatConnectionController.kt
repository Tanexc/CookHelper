package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller

import io.ktor.server.websocket.*
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.*
import ru.tanec.cookhelper.domain.model.Message
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository.ChatConnectionRepository

class ChatConnectionController(
    val repository: ChatConnectionRepository
) {
    fun connect(
        session: DefaultWebSocketServerSession,
        chatId: Long?
    ): State<out Any> {

        return if (chatId == null) {
            State.Error(REQUIRED("id"))
        } else {
            repository.add(chatId, session)
            State.Success()
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

                return State.Success(SUCCESS)

            }

            else -> {return State.Error()}
        }

    }

    fun disconnect(
        session: DefaultWebSocketServerSession,
        chatId: Long?
    ): State<out Any> {
        return if (chatId == null) {
            State.Error(REQUIRED("id"))
        } else {
            repository.del(chatId, session)
            State.Success(CLOSED)
        }
    }

    fun connected(
        session: DefaultWebSocketServerSession,
        chatId: Long
    ): Boolean {
        return repository.get(chatId, session).data?.contains(session) ?: false
    }
}