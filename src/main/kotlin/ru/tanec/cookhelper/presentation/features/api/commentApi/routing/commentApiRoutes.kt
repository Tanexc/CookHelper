package ru.tanec.cookhelper.presentation.features.api.commentApi.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.CommentRepository
import ru.tanec.cookhelper.presentation.features.api.commentApi.use_case.GetCommentsUseCase

fun commentApiRoutes(
    route: Routing,
    commentRepository: CommentRepository
) {
    route.get("api/comment/get") {
        call.respond(GetCommentsUseCase(commentRepository, call.request.queryParameters))
    }
}