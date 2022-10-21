package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.messageDao.MessageDao
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

interface MessageRepository {

    val dao: MessageDao

    fun getByIdList(listId: List<Long>, part: Int?, div: Int?): Flow<State<List<Message>?>>

    fun getById(id: Long): Flow<State<Message?>>

    fun insert(message: Message): Flow<State<Message?>>
}