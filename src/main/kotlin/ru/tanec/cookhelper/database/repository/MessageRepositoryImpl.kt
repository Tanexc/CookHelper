package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.database.dao.messageDao.MessageDao
import ru.tanec.cookhelper.database.dao.messageDao.MessageDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository

class MessageRepositoryImpl(override val dao: MessageDao = MessageDaoImpl()) : MessageRepository {
    override fun getByIdList(listId: List<Long>, part: Int?, div: Int?): Flow<State<List<Message>?>> = flow {
        emit(State.Processing())
        try {

            emit(State.Success(dao.getByListId(listId, part, div)))

        } catch (e: Exception) {
            emit(State.Error(message=e.message?: "exception in getByIdList() of MessageRepository"))
        }
    }

    override fun getByOffset(listId: List<Long>, offset: Int, limit: Int): Flow<State<List<Message>?>> = flow {
        emit(State.Processing())
        try {

            emit(State.Success(dao.getByOffset(listId, offset, limit)))

        } catch (e: Exception) {
            emit(State.Error(message=e.message?: "exception in getByIdList() of MessageRepository"))
        }
    }

    override fun getById(id: Long): Flow<State<Message?>> = flow {
        emit(State.Processing())
        try {

            val data = dao.getById(id)

            if (data != null) {
                emit(State.Success(data=data))
            } else emit(State.Error(data=null, status=MESSAGE_NOT_FOUND))

        } catch (e: Exception) {
            emit(State.Error(message=e.message?: "exception in getById() of MessageRepository"))
        }
    }

    override fun insert(message: Message): Flow<State<Message?>> = flow {
        emit(State.Processing())
        try {

            val data = dao.insert(message)

            if (data != null) {
                emit(State.Success(data=data))
            } else emit(State.Error(data=null, status=MESSAGE_NOT_CREATED))

        } catch (e: Exception) {
            emit(State.Error(message=e.message?: "exception in insert() of MessageRepository"))
        }
    }
}