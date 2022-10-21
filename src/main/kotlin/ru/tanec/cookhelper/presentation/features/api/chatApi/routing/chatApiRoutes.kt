package ru.tanec.cookhelper.presentation.features.api.chatApi.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.api.chatApi.use_case.CreateChatUseCase
import ru.tanec.cookhelper.presentation.features.api.chatApi.use_case.GetChatUseCase

fun chatApiRoutes(
    route: Routing,
    repository: ChatRepository,
    userRepository: UserRepository,
    messageRepository: MessageRepository
    ) {

    route.post("/api/chat/post/create") {
        call.respond(CreateChatUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
    }

    route.get("/api/chat/get/by-id") {
        call.respond(GetChatUseCase(repository, userRepository, messageRepository, call.request.queryParameters))
    }

}