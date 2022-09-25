package ru.tanec.cookhelper.core.plugins

import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.plugins.conditionalheaders.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*

fun Application.configureHTTP() {
    install(HttpsRedirect)
    install(ConditionalHeaders)

}
