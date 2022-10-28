package ru.tanec.cookhelper.presentation.features.api.feedApi.routing

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostCreateUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetByUserUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetUseCase

fun feedApiRoutes(
    route: Routing,
    repository: PostRepository,
    userRepository: UserRepository
) {
    route.post("/api/feed/post/create/") {
        call.respond(PostCreateUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
    }

    route.get("/api/feed/get/by-id/") {
        call.respond(PostGetByIdUseCase(repository, call.request.queryParameters))
    }

    route.get("api/feed/get/") {
        // get posts by list of postID
        call.respond(PostGetUseCase(repository, call.request.queryParameters))
    }

    route.get("api/feed/get/by-user/") {
        // get posts by user token
        call.respond(PostGetByUserUseCase(repository, userRepository, call.request.queryParameters))
    }



}