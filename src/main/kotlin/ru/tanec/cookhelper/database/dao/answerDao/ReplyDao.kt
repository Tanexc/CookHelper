package ru.tanec.cookhelper.database.dao.answerDao

import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply

interface ReplyDao {
    suspend fun getById(id: Long): Reply?

    suspend fun getByListId(listId: List<Long>): List<Reply>

    suspend fun getByListId(listId: List<Long>, part: Int, div: Int): List<Reply>

    suspend fun insert(reply: Reply): Reply?
}