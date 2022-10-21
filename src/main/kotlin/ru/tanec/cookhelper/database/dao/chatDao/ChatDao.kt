package ru.tanec.cookhelper.database.dao.chatDao

import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

interface ChatDao {
    suspend fun insert(chat: Chat): Chat?

    suspend fun getById(id: Long): Chat?

    suspend fun getByList(listId: List<Long>, part: Int?, div: Int?): List<Chat>

    suspend fun edit(chat: Chat): Chat?
}