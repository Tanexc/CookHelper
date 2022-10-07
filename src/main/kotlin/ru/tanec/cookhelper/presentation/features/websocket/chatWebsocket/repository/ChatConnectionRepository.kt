package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository

import io.ktor.server.websocket.*
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.repository.ConnectionRepository


class ChatConnectionRepository(

    override val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf()

) : ConnectionRepository<Long> {

    override fun add(unit: Long, session: DefaultWebSocketServerSession) {
        if (data[unit] == null) {
            data[unit] = mutableListOf()
        }
        data[unit]?.add(session)
    }

    override fun del(unit: Long, session: DefaultWebSocketServerSession) {
        if (data[unit] == null) {
            data[unit] = mutableListOf()
        }
        data[unit]?.remove(session)
    }

    override fun get(unit: Long, session: DefaultWebSocketServerSession): State<MutableList<DefaultWebSocketServerSession>> {
        if (data[unit] == null) {
            data[unit] = mutableListOf()
        }
        return State.Success(data = data[unit], status=300)
    }

}