package ru.tanec.cookhelper.presentation.features.api.forumApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.forumApi.*

fun Routing.forumApiRoutes() {

    val topicRepository: TopicRepository by inject()
    val userRepository: UserRepository by inject()

    post("api/forum/post/topic/create/") {
        call.respond(CreateTopicUseCase(topicRepository, userRepository, call.receiveMultipart().readAllParts()))
    }

    get("api/forum/get/topic/by-problem/") {
        call.respond(GetTopicByProblemUseCase(topicRepository, userRepository, call.request.queryParameters))
    }


    get("api/forum/get/topic/by-title/") {
        call.respond(GetTopicByTitleUseCase(topicRepository, userRepository, call.request.queryParameters))
    }

    get("api/forum/get/topic/by-list/") {
        call.respond(GetTopicByListId(topicRepository, userRepository, call.request.queryParameters))
    }

    get("api/forum/get/topic/by-id/") {
        call.respond(GetTopicByIdUseCase(topicRepository, userRepository, call.request.queryParameters))
    }
}