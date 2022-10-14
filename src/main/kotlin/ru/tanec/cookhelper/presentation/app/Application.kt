package ru.tanec.cookhelper.presentation.app

import ru.tanec.cookhelper.core.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.tanec.cookhelper.core.plugins.configureRouting
import ru.tanec.cookhelper.presentation.routing.routes
import ru.tanec.cookhelper.core.db.factory.DatabaseFactory

fun main() {
    embeddedServer(Netty, host = "127.0.0.1", port=5000) {
        DatabaseFactory.init()
        configureAdministration()
        configureSockets()
        configureSerialization()
        //configureHTTP()
        configureRouting()
        configureDI()
        routes()



    }.start(wait = true)
}
