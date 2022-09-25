package ru.tanec.cookhelper.domain.repository

import io.ktor.server.websocket.*
import ru.tanec.cookhelper.core.State

interface ConnectionRepository<T: Any> {

    val data:  MutableMap<T, MutableList<DefaultWebSocketServerSession>>

    fun add(unit: T, session: DefaultWebSocketServerSession)

    fun del(unit: T, session: DefaultWebSocketServerSession)

    fun get(unit: T, session: DefaultWebSocketServerSession): State<MutableList<DefaultWebSocketServerSession>>

}