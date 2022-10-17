package ru.tanec.cookhelper.presentation.features.api.commentApi.use_case

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository

object GetCommentsUseCase {
    suspend operator fun invoke(
        commentRepository: CommentRepository,
        parameters: Parameters
    ): ApiResponse<List<Comment>?> {
        val comments = parameters["commentId"]?.split(" ")?.mapNotNull {it.toLongOrNull()}?: listOf()
        val state = commentRepository.getByIdList(comments).last()
        return state.asApiResponse()
    }
}