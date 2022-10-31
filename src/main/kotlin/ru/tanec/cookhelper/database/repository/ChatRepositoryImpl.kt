package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.CHAT_NOT_FOUND
import ru.tanec.cookhelper.database.dao.chatDao.ChatDao
import ru.tanec.cookhelper.database.dao.chatDao.ChatDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository

class ChatRepositoryImpl(override val dao: ChatDao = ChatDaoImpl()) : ChatRepository {
    override fun getChatById(id: Long): Flow<State<Chat?>> = flow {
       try {
            emit(State.Processing())
           when (val chat = dao.getById(id)) {
                is Chat -> emit(State.Success(data = chat))
                else -> emit(State.Error(status = CHAT_NOT_FOUND))
            }
        } catch (e: Exception) {
           emit(State.Error(message=e.message?:"exception in getChatById() of ChatRepository"))
       }

    }

    override fun getChatByIdList(listId: List<Long>, limit: Int?, offset: Int?): Flow<State<List<Chat>?>> = flow {
        try {
            emit(State.Processing())
            val chat = dao.getByList(listId, limit, offset)
            emit(State.Success(data = chat))
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"exception in getChatByIdList() of ChatRepository"))
        }
    }

    override fun insert(chat: Chat): Flow<State<Chat?>> = flow {
        try {
            emit(State.Processing())
            when (val data = dao.insert(chat)) {
                is Chat -> emit(State.Success(data=data))
                else -> emit(State.Error(status=CHAT_NOT_FOUND))
            }
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"exception in insert() of ChatRepository"))
        }
    }

    override fun editChat(chat: Chat): Flow<State<Chat?>> = flow {
        try {
            emit(State.Processing())
            when (val data = dao.edit(chat)) {
                is Chat -> emit(State.Success(data=data))
                else -> emit(State.Error(status=CHAT_NOT_FOUND))
            }
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"exception in editChat() of ChatRepository"))
        }
    }
}