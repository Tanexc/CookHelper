package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.messageDao.MessageDao
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import java.time.OffsetDateTime

interface MessageRepository {

    val dao: MessageDao

    fun getByIdList(listId: List<Long>, part: Int?, div: Int?): Flow<State<List<Message>?>>

    fun getByOffset(listId: List<Long>, offset: Int, limit:Int): Flow<State<List<Message>?>>

    fun getById(id: Long): Flow<State<Message?>>

    fun insert(message: Message): Flow<State<Message?>>
    abstract suspend fun getByListId(listId: List<Long>): List<Message>
}