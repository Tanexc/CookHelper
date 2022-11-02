package ru.tanec.cookhelper.presentation.features.api.chatApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.chatApi.CreateChatUseCase
import ru.tanec.cookhelper.enterprise.use_case.chatApi.GetChatByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.chatApi.GetChatByListUseCase
import ru.tanec.cookhelper.enterprise.use_case.messageApi.GetMessagesUseCase

fun Routing.chatApiRoutes() {

    val chatRepository: ChatRepository by inject()
    val messageRepository: MessageRepository by inject()
    val userRepository: UserRepository by inject()


    post("/api/chat/post/create/") {
        call.respond(CreateChatUseCase(chatRepository, userRepository, call.receiveMultipart().readAllParts()))
    }

    get("/api/chat/get/by-list-id/") {
        call.respond(GetChatByListUseCase(chatRepository, userRepository, call.request.queryParameters))
    }

    get("/api/chat/get/messages/") {
        call.respond(GetMessagesUseCase(messageRepository, userRepository, chatRepository, call.request.queryParameters))
    }

    get("/api/chat/get/by-id/") {
        call.respond(GetChatByIdUseCase(chatRepository, userRepository, messageRepository, call.request.queryParameters))
    }

}