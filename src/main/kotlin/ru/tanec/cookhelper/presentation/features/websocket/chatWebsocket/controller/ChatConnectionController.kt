package ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.ANSWER_NOT_ADDED
import ru.tanec.cookhelper.core.constants.status.CHAT_NOT_FOUND
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.database.dao.chatDao.ChatDao
import ru.tanec.cookhelper.database.dao.chatDao.ChatDaoImpl
import ru.tanec.cookhelper.database.dao.messageDao.MessageDao
import ru.tanec.cookhelper.database.dao.messageDao.MessageDaoImpl
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.database.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket.ChatReceiveMessageData
import ru.tanec.cookhelper.enterprise.model.response.MessageResponseData

class ChatConnectionController(
    val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf(),
    private val chatDao: ChatDao = ChatDaoImpl(),
    private val messageDao: MessageDao = MessageDaoImpl(),
    private val userDao: UserDao = UserDaoImpl(),
) {
    fun connect(
        session: DefaultWebSocketServerSession,
        parameters: Parameters
    ): Flow<State<Chat?>> = flow {

        val id = parameters["id"]?.toLongOrNull()
        val token = parameters["token"]

        when (id == null) {
            true -> emit(State.Error(data = null, status = PARAMETER_MISSED))
            false -> {
                if (token != null) {
                    val user = userDao.getByToken(token)?: emit(
                        State.Error(
                            data=null,
                            status= USER_TOKEN_INVALID
                        )
                    )
                    val chat = chatDao.getById(id)

                    if (chat != null) {

                        data[id] = (data[id]?.plus(listOf(session)))?.toMutableList()?: mutableListOf(session)


                        emit(State.Success(data = chat, addition = user))
                    } else emit(
                        State.Error(
                            data = null,
                            status = CHAT_NOT_FOUND
                        )
                    )

                } else emit(State.Error(data = null, status = PARAMETER_MISSED))
            }
        }

    }

    suspend fun sendMessage(
        user: User,
        chatId: Long,
        message: Message
    ) {
        val response = MessageResponseData(
            id=message.id,
            author=user.smallInfo(),
            text=message.text,
            attachments=message.attachments,
            replyToId=message.replyToId,
            timestamp=message.timestamp,
        )

        val chat = chatDao.getById(chatId)

        chat?.let {chatDao.edit(it.copy(messages = it.messages + listOf(message.id)))}

        for (receiver: DefaultWebSocketServerSession in data[chatId]?: listOf()) {
            receiver.sendSerialized(response)
        }

    }

    fun disconnect(session: DefaultWebSocketServerSession, id: Long): Boolean {
        return data[id]?.remove(session)?: return true
    }

    suspend fun receiveMessage(
        data: ChatReceiveMessageData,
        user: User
    ): State<Message?> {
        val processedData = messageDao.insert(data.asDomain(user.id, getTimeMillis()))?: return State.Error(status= ANSWER_NOT_ADDED)
        return State.Success(data = processedData, status= ru.tanec.cookhelper.core.constants.status.SUCCESS)

    }

}