package ru.tanec.cookhelper.presentation.app

import ru.tanec.cookhelper.core.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import ru.tanec.cookhelper.core.plugins.configureRouting
import ru.tanec.cookhelper.presentation.routing.routes
import ru.tanec.cookhelper.core.db.factory.DatabaseFactory

fun main() {

    embeddedServer(Netty) {
        DatabaseFactory.init()
        configureAdministration()
        configureSockets()
        configureSerialization()
        //configureHTTP()
        //configureSecurity()
        configureRouting()
        configureDI()
        routes()



    }.start(wait = true)
}
