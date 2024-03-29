package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.chatDao.ChatDao
import ru.tanec.cookhelper.database.dao.messageDao.MessageDao
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

interface ChatRepository {
    val dao: ChatDao
    val messageDao: MessageDao

    fun getChatById(id: Long): Flow<State<Chat?>>

    fun insert(chat: Chat): Flow<State<Chat?>>

    fun editChat(chat: Chat): Flow<State<Chat?>>

    fun getChatByIdList(listId: List<Long>, limit: Int?, offset: Int?): Flow<State<List<Chat>?>>

    suspend fun getChatMessages(id: Long, offset: Int?, limit: Int?): List<Long>?

    suspend fun getMessages(messagesId: List<Long>): List<Message>
}