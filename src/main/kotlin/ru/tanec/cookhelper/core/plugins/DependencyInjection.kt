package ru.tanec.cookhelper.core.plugins

import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import ru.tanec.cookhelper.core.di.apiRepositoryModule
import ru.tanec.cookhelper.core.di.socketModule

fun Application.configureDI() {
    install(Koin) {
        modules(
            socketModule,
            apiRepositoryModule
        )
    }
}