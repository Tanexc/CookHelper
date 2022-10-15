package ru.tanec.cookhelper.database.dao.commentDao

import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment

interface CommentDao {
    suspend fun getById(id: Long): Comment?

    suspend fun getByIdList(list: List<Long>): List<Comment>

    suspend fun insert(comment: Comment): Comment?
}