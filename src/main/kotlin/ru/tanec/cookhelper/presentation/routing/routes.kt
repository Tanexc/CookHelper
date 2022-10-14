package ru.tanec.cookhelper.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.*
import ru.tanec.cookhelper.presentation.features.api.categoryApi.routing.categoryApiRoutes
import ru.tanec.cookhelper.presentation.features.api.feedApi.routing.feedApiRoutes
import ru.tanec.cookhelper.presentation.features.api.ingredientApi.routing.ingredientApiRoutes
import ru.tanec.cookhelper.presentation.features.api.recipeApi.routing.recipeApiRoutes
import ru.tanec.cookhelper.presentation.features.api.userApi.userApiRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.chatWebsocket
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import java.io.File

fun Application.routes() {
    routing {

        val userRepository: UserRepository by inject()
        val recipeRepository: RecipeRepository by inject()
        val postRepository: PostRepository by inject()
        val ingredientRepository: IngredientRepository by inject()
        val categoryRepository: CategoryRepository by inject()

        userApiRoutes(this, userRepository)
        recipeApiRoutes(this, recipeRepository)
        feedApiRoutes(this, postRepository)
        categoryApiRoutes(this, categoryRepository)
        ingredientApiRoutes(this, ingredientRepository)

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