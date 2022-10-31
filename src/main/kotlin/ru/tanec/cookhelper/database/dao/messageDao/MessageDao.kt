package ru.tanec.cookhelper.database.dao.messageDao

import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

interface MessageDao {
    suspend fun getById(id: Long): Message?

    suspend fun getByListId(listId: List<Long>): List<Message>

    suspend fun getByListId(listId: List<Long>, part: Int?, div: Int?): List<Message>

    suspend fun insert(message: Message): Message?

    suspend fun getByOffset(listId: List<Long>, offset: Int, limit: Int): List<Message>


}