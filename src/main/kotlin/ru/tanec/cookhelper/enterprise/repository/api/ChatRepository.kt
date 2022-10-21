package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.chatDao.ChatDao
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

interface ChatRepository {
    val dao: ChatDao

    fun getChatById(id: Long): Flow<State<Chat?>>

    fun insert(chat: Chat): Flow<State<Chat?>>

    fun editChat(chat: Chat): Flow<State<Chat?>>

    fun getChatByIdList(listId: List<Long>, part: Int?, div: Int?): Flow<State<List<Chat>?>>
}