package ru.tanec.cookhelper.core.plugins

import io.ktor.server.engine.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import ru.tanec.cookhelper.database.factory.DatabaseFactory

fun Application.configureAdministration() {
    DatabaseFactory.init()
    install(ShutDownUrl.ApplicationCallPlugin) {
        // The URL that will be intercepted (you can also use the application.conf's ktor.deployment.shutdown.url key)
        shutDownUrl = "/ktor/application/shutdown"
        // A function that will be executed to get the exit code of the process
        exitCodeSupplier = { 0 } // ApplicationCall.() -> Int
    }

}
