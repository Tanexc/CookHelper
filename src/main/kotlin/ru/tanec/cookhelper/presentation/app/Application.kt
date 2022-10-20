package ru.tanec.cookhelper.presentation.app

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.tanec.cookhelper.core.plugins.*
import ru.tanec.cookhelper.database.factory.DatabaseFactory
import ru.tanec.cookhelper.presentation.routing.apiRoutes

fun main() {
    embeddedServer(Netty, port = 8080) {
        DatabaseFactory.init()
        configureAdministration()
        configureSockets()
        configureSerialization()
        configureRouting()
        configureDI()
        apiRoutes()
    }.start(wait = true)
}

