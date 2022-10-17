package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.commentDao.CommentDao
import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment

interface CommentRepository {

    val dao: CommentDao

    fun getById(id: Long): Flow<State<Comment?>>

    fun getByIdList(list: List<Long>): Flow<State<List<Comment>?>>

    fun insertComment(comment: Comment): Flow<State<Comment?>>
}