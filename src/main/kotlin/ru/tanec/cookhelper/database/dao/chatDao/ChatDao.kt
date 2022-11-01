package ru.tanec.cookhelper.database.dao.chatDao

import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

interface ChatDao {
    suspend fun insert(chat: Chat): Chat?

    suspend fun getById(id: Long): Chat?

    suspend fun getByList(listId: List<Long>, limit: Int?, offset: Int?): List<Chat>

    suspend fun edit(chat: Chat): Chat?

    suspend fun getChatMessages(id: Long, offset: Int?, limit: Int?): List<Long>?
}