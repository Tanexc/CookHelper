package ru.tanec.cookhelper.presentation.features.api.forumApi.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.forumApi.CreateTopicUseCase
import ru.tanec.cookhelper.enterprise.use_case.forumApi.GetTopicByListId
import ru.tanec.cookhelper.enterprise.use_case.forumApi.GetTopicByProblemUseCase
import ru.tanec.cookhelper.enterprise.use_case.forumApi.GetTopicByTitleUseCase

fun forumApiRoutes(
    route: Routing,
    repository: TopicRepository,
    userRepository: UserRepository
) {

    route.post("api/forum/post/create-topic/") {
        call.respond(CreateTopicUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
    }

    route.get("api/forum/get/topic/by-problem/") {
        call.respond(GetTopicByProblemUseCase(repository, userRepository, call.request.queryParameters))
    }


    route.get("api/forum/get/topic/by-title/") {
        call.respond(GetTopicByTitleUseCase(repository, userRepository, call.request.queryParameters))
    }

    route.get("api/forum/get/topic/by-id/") {
        call.respond(GetTopicByListId(repository, userRepository, call.request.queryParameters))
    }
}