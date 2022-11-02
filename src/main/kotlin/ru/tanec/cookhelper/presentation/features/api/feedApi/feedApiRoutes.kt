package ru.tanec.cookhelper.presentation.features.api.feedApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostCreateUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetByUserUseCase
import ru.tanec.cookhelper.enterprise.use_case.feedApi.PostGetUseCase

fun Routing.feedApiRoutes() {

    val postRepository: PostRepository by inject()
    val userRepository: UserRepository by inject()


    post("/api/feed/post/create/") {
        call.respond(PostCreateUseCase(postRepository, userRepository, call.receiveMultipart().readAllParts()))
    }

    get("/api/feed/get/by-id/") {
        call.respond(PostGetByIdUseCase(postRepository, call.request.queryParameters))
    }

    get("api/feed/get/") {
        // get posts by list of postID
        call.respond(PostGetUseCase(postRepository, call.request.queryParameters))
    }

    get("api/feed/get/by-user/") {
        // get posts by user token
        call.respond(PostGetByUserUseCase(postRepository, userRepository, call.request.queryParameters))
    }



}