package ru.tanec.cookhelper.presentation.app

import ru.tanec.cookhelper.core.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
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
        configureRouting()
        configureDI()
        routes()



    }.start(wait = true)
}
