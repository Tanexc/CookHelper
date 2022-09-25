package ru.tanec.cookhelper.core.di

import io.ktor.server.websocket.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.factory
import org.koin.dsl.module
import ru.tanec.cookhelper.domain.repository.ConnectionRepository
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.manager.ChatConnectionManager
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository.ChatConnectionRepository

val managerModule = module {
    singleOf(::ChatConnectionManager)
    singleOf(::ChatConnectionRepository) { bind<ConnectionRepository<Long>>() }
    single { mutableMapOf<Long, MutableList<DefaultWebSocketServerSession>>() }
}