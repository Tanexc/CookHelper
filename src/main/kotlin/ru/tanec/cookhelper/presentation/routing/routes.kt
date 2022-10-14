package ru.tanec.cookhelper.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.PostRepository
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.UserRepository
import ru.tanec.cookhelper.presentation.features.api.feedApi.routing.feedApiRoutes
import ru.tanec.cookhelper.presentation.features.api.recipeApi.routing.recipeApiRoutes
import ru.tanec.cookhelper.presentation.features.api.userApi.userApiRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.chatWebsocket
import java.io.File

fun Application.routes() {
    routing {

        val userRepository: UserRepository by inject()
        val recipeRepository: RecipeRepository by inject()
        val postRepository: PostRepository by inject()

        userApiRoutes(this, userRepository)
        recipeApiRoutes(this, recipeRepository)
        feedApiRoutes(this, postRepository)

        static("/static") {
            resources("static")
        }

        static("/file") {
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
        }

        webSocket("/messenger/{token}") {
            val token = call.parameters["token"]
        }

        webSocket("/chat/{id}") {
            val manager: ChatConnectionController by inject()
            chatWebsocket(
                this,
                incoming,
                manager,
                call.request.queryParameters["token"],
                call.parameters["id"]?.toLong()
            )
        }
    }
}