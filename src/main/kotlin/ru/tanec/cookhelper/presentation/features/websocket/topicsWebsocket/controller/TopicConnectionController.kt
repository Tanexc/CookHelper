package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.constants.status.TopicStatus
import ru.tanec.cookhelper.database.dao.answerDao.AnswerDao
import ru.tanec.cookhelper.database.dao.answerDao.AnswerDaoImpl
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDaoImpl
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.database.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.forum.Answer
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveAnswerData
import ru.tanec.cookhelper.enterprise.model.response.AnswerResponseData

class TopicConnectionController {
    val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf()
    val topicDao: TopicDao = TopicDaoImpl()
    val answerDao: AnswerDao = AnswerDaoImpl()
    val userDao: UserDao = UserDaoImpl()

    suspend fun connect(
        session: DefaultWebSocketServerSession,
        parameters: Parameters
    ): Flow<State<Topic?>> = flow {
        emit(State.Processing())

        val id = parameters["id"]?.toLongOrNull()
        val token = parameters["token"]

        when (id == null) {
            true -> emit(State.Error(data=null, status=PARAMETER_MISSED))
            else -> {
                val topic = topicDao.getById(id)
                if (token == null) {
                    emit(State.Error(data=null, status=PARAMETER_MISSED))
                } else if (topic == null) {
                    emit(State.Error(data=null, status=TOPIC_NOT_FOUND))
                } else {
                    val sessionData = data[id]?: mutableListOf()
                    sessionData.add(session)
                    data[id] = sessionData
                    val user = userDao.getByToken(token)
                    emit(State.Success(data=topic, addition=user, status=SUCCESS))
                }
            }

        }


    }

    fun disconnect(session: DefaultWebSocketServerSession, id: Long): Boolean {
           return data[id]?.remove(session)?: return true
    }

    suspend fun sendMessage(
        answer: Answer,
        user: User,
        topicId: Long
    ) {

        val response = AnswerResponseData(
            id=answer.id,
            authorId=user.id,
            text=answer.text,
            attachments=answer.attachments,
            replyToId=answer.replyToId,
            timestamp=answer.timestamp,
            likes=answer.likes,
            name=user.name,
            nickname=user.nickname,
            surname=user.surname,
            avatar=user.avatar[0]
        )

        for (receiver: DefaultWebSocketServerSession in data[topicId]?: listOf()) {
            receiver.sendSerialized(response)
        }


    }

    suspend fun receiveMessage(
        data: ForumReceiveAnswerData,
        user: User
    ): State<Answer?> {
        val processedData = answerDao.insert(data.asDomain(user.id, listOf(), getTimeMillis()))?: return State.Error(status=ANSWER_NOT_ADDED)
        return State.Success(data = processedData, status=SUCCESS)

    }
}