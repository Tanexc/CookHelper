package ru.tanec.cookhelper.presentation.features.api.commentApi

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository
import ru.tanec.cookhelper.enterprise.use_case.commentApi.GetCommentsUseCase

fun Routing.commentApiRoutes() {

    val commentRepository: CommentRepository by inject()

    get("api/comment/get") {
        call.respond(GetCommentsUseCase(commentRepository, call.request.queryParameters))
    }
}