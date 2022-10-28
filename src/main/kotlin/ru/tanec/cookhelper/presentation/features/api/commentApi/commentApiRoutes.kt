package ru.tanec.cookhelper.presentation.features.api.commentApi.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository
import ru.tanec.cookhelper.enterprise.use_case.commentApi.GetCommentsUseCase

fun commentApiRoutes(
    route: Routing,
    commentRepository: CommentRepository
) {
    route.get("api/comment/get") {
        call.respond(GetCommentsUseCase(commentRepository, call.request.queryParameters))
    }
}