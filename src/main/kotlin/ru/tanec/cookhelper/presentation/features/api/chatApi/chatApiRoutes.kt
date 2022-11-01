package ru.tanec.cookhelper.presentation.features.api.chatApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.chatApi.CreateChatUseCase
import ru.tanec.cookhelper.enterprise.use_case.chatApi.GetChatByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.chatApi.GetChatByListUseCase
import ru.tanec.cookhelper.enterprise.use_case.messageApi.GetMessagesUseCase

fun chatApiRoutes(
    route: Routing,
    repository: ChatRepository,
    userRepository: UserRepository,
    messageRepository: MessageRepository
    ) {

    route.post("/api/chat/post/create/") {
        call.respond(CreateChatUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
    }

    route.get("/api/chat/get/by-list-id/") {
        call.respond(GetChatByListUseCase(repository, userRepository, call.request.queryParameters))
    }

    route.get("/api/chat/get/messages/") {
        call.respond(GetMessagesUseCase(messageRepository, userRepository, repository, call.request.queryParameters))
    }

    route.get("/api/chat/get/by-id/") {
        call.respond(GetChatByIdUseCase(repository, userRepository, messageRepository, call.request.queryParameters))
    }

}