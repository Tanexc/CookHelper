package ru.tanec.cookhelper.core.plugins

import io.ktor.server.engine.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin
import ru.tanec.cookhelper.core.di.apiRepositoryModule
import ru.tanec.cookhelper.core.di.managerModule

fun Application.configureDI() {
    install(Koin) {
        modules(
            managerModule,
            apiRepositoryModule
        )
    }
}