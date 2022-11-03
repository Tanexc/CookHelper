package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDao
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDaoImpl
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDaoImpl
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.database.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveReplyData
import ru.tanec.cookhelper.enterprise.model.response.ReplyResponseData

class TopicConnectionController {
    val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf()
    private val topicDao: TopicDao = TopicDaoImpl()
    private val replyDao: ReplyDao = ReplyDaoImpl()
    private val userDao: UserDao = UserDaoImpl()

    suspend fun connect(
        session: DefaultWebSocketServerSession,
        parameters: Parameters
    ): Flow<State<Topic?>> = flow {

        val id = parameters["id"]?.toLongOrNull()
        val token = parameters["token"]
        val user = userDao.getByToken(token ?: "")
        val topic = topicDao.getById(id ?: -1)

        when (id == null || token == null) {
            true -> emit(State.Error(data = topic, status = PARAMETER_MISSED))
            false -> {
                if (user == null) emit(State.Error(status = USER_TOKEN_INVALID))
                else if (topic == null) emit(State.Error(status = TOPIC_NOT_FOUND))
                else {
                    if (!user.topics.contains(topic.id)) userDao.editUser(user.copy(topics = user.topics.plus(topic.id)))
                    data[id] = (data[id]?.plus(listOf(session)))?.toMutableList() ?: mutableListOf(session)
                    emit(State.Success(data = topic, addition = user))
                }
            }
        }
    }

    fun disconnect(session: DefaultWebSocketServerSession, id: Long?): Boolean {
        if (id != null) {
            return data[id]?.remove(session) ?: return true
        } else return true
    }

    suspend fun sendMessage(
        reply: Reply,
        user: User,
        topicId: Long
    ) {

        val response = ReplyResponseData(
            id = reply.id,
            author = user.smallInfo(),
            text = reply.text,
            attachments = reply.attachments,
            replyToId = reply.replyToId,
            timestamp = reply.timestamp,
            ratingNegative = reply.ratingNegative,
            ratingPositive = reply.ratingPositive,
            replies = reply.replies.mapNotNull { replyDao.getById(it) }
        )

        for (receiver: DefaultWebSocketServerSession in data[topicId] ?: listOf()) {
            receiver.sendSerialized(response)
        }
    }

    suspend fun receiveMessage(
        data: ForumReceiveReplyData,
        user: User
    ): State<Reply?> {
        val processedData = replyDao.insert(data.asDomain(user.id, listOf(), listOf(), getTimeMillis()))
            ?: return State.Error(status = ANSWER_NOT_ADDED)
        return State.Success(data = processedData, status = SUCCESS)

    }
}