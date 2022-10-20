package ru.tanec.cookhelper.presentation.app

import ru.tanec.cookhelper.core.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.tanec.cookhelper.core.plugins.configureRouting
import ru.tanec.cookhelper.presentation.routing.routes
import ru.tanec.cookhelper.database.factory.DatabaseFactory

class Application {
    fun main() {
        embeddedServer(Netty, port = 8080) {
            DatabaseFactory.init()
            configureAdministration()
            configureSockets()
            configureSerialization()
            configureRouting()
            configureDI()
            routes()


        }.start(wait = true)
    }
}