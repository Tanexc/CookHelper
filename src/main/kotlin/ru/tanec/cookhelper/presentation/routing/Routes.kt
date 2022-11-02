package ru.tanec.cookhelper.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.*
import ru.tanec.cookhelper.presentation.features.api.categoryApi.categoryApiRoutes
import ru.tanec.cookhelper.presentation.features.api.chatApi.chatApiRoutes
import ru.tanec.cookhelper.presentation.features.api.commentApi.commentApiRoutes
import ru.tanec.cookhelper.presentation.features.api.feedApi.feedApiRoutes
import ru.tanec.cookhelper.presentation.features.api.forumApi.forumApiRoutes
import ru.tanec.cookhelper.presentation.features.api.ingredientApi.ingredientApiRoutes
import ru.tanec.cookhelper.presentation.features.api.recipeApi.routing.recipeApiRoutes
import ru.tanec.cookhelper.presentation.features.api.userApi.userApiRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.routing.chatWebsocketRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.routing.topicWebsocketRoutes
import java.io.File

fun Application.apiRoutes() {
    routing {

        val topicConnectionController: TopicConnectionController by inject()


        userApiRoutes()
        recipeApiRoutes()
        feedApiRoutes()
        categoryApiRoutes()
        ingredientApiRoutes()
        commentApiRoutes()
        forumApiRoutes()
        chatApiRoutes()

        topicWebsocketRoutes()
        chatWebsocketRoutes()

        static("/static") {
            resources("static")
        }

        static("/data") {
            staticRootFolder = File("data")
            static("recipe") {
                files("recipe")
            }
            static("user") {
                files("user")
            }
            static("feed") {
                files("feed")
            }
            static("attachment") {
                files("attachment")
            }
            static("chat") {
                files("chat")
            }
        }
    }
}