package ru.tanec.cookhelper.presentation.routing

import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.*
import ru.tanec.cookhelper.presentation.features.api.categoryApi.routing.categoryApiRoutes
import ru.tanec.cookhelper.presentation.features.api.chatApi.chatApiRoutes
import ru.tanec.cookhelper.presentation.features.api.commentApi.routing.commentApiRoutes
import ru.tanec.cookhelper.presentation.features.api.feedApi.routing.feedApiRoutes
import ru.tanec.cookhelper.presentation.features.api.forumApi.forumApiRoutes
import ru.tanec.cookhelper.presentation.features.api.ingredientApi.routing.ingredientApiRoutes
import ru.tanec.cookhelper.presentation.features.api.recipeApi.routing.recipeApiRoutes
import ru.tanec.cookhelper.presentation.features.api.userApi.userApiRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.routing.chatWebsocketRoutes
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.routing.topicWebsocketRoutes
import java.io.File

fun Application.apiRoutes() {
    routing {

        val userRepository: UserRepository by inject()
        val recipeRepository: RecipeRepository by inject()
        val postRepository: PostRepository by inject()
        val ingredientRepository: IngredientRepository by inject()
        val categoryRepository: CategoryRepository by inject()
        val commentRepository: CommentRepository by inject()
        val topicRepository: TopicRepository by inject()
        val chatRepository: ChatRepository by inject()
        val messageRepository: MessageRepository by inject()

        val topicConnectionController: TopicConnectionController by inject()
        val chatConnectionController: ChatConnectionController by inject()

        userApiRoutes(this, userRepository)
        recipeApiRoutes(this, recipeRepository, userRepository, commentRepository)
        feedApiRoutes(this, postRepository, userRepository)
        categoryApiRoutes(this, categoryRepository)
        ingredientApiRoutes(this, ingredientRepository)
        commentApiRoutes(this, commentRepository)
        forumApiRoutes(this, topicRepository, userRepository)
        chatApiRoutes(this, chatRepository, userRepository, messageRepository)

        topicWebsocketRoutes(this, topicConnectionController)
        chatWebsocketRoutes(this, chatConnectionController)

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

        static("/build") {
            staticRootFolder = File("build")
            files("build")
        }
    }
}