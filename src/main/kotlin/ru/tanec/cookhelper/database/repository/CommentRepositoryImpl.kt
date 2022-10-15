package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.CommentStatus
import ru.tanec.cookhelper.database.dao.commentDao.CommentDao
import ru.tanec.cookhelper.database.dao.commentDao.CommentDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment
import ru.tanec.cookhelper.enterprise.repository.CommentRepository

class CommentRepositoryImpl(
    override val dao: CommentDao = CommentDaoImpl()
) : CommentRepository {

    override fun getById(id: Long): Flow<State<Comment?>> = flow {
        emit(State.Processing())
        try {
            val data: Comment? = dao.getById(id)

            if (data == null) {
                emit(State.Error(status = CommentStatus.COMMENT_NOT_FOUND))
            }

            emit(State.Success(status = CommentStatus.SUCCESS, data = data))

        } catch (e: Exception) {
            emit(State.Error(status = CommentStatus.EXCEPTION, message = e.message ?: "error"))
        }

    }

    override fun getByIdList(list: List<Long>): Flow<State<List<Comment>?>> = flow {
        emit(State.Processing())
        try {
            val data: List<Comment> = dao.getByIdList(list)
            emit(State.Success(status = CommentStatus.SUCCESS, data = data))

        } catch (e: Exception) {
            emit(State.Error(status = CommentStatus.EXCEPTION, message = e.message ?: "error"))
        }

    }

    override fun insertComment(comment: Comment): Flow<State<Comment?>> = flow {
        emit(State.Processing())
        try {
            val data: Comment? = dao.insert(comment)

            if (data == null) {
                emit(State.Error(status = CommentStatus.PARAMETER_MISSED))
            }

            emit(State.Success(status = CommentStatus.SUCCESS, data = data))

        } catch (e: Exception) {
            emit(State.Error(status = CommentStatus.EXCEPTION, message = e.message ?: "error"))
        }

    }
}