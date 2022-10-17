package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.TopicStatus
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse

class TopicConnectionController {
    val data: MutableMap<Long, MutableList<DefaultWebSocketServerSession>> = mutableMapOf()

    suspend fun DefaultWebSocketServerSession.connect(
        parameters: Parameters,
        topicDao: TopicDao
    ): Flow<State<Topic?>> = flow {
        emit(State.Processing())

        val id = parameters["id"]?.toLongOrNull()

        when (id == null) {
            true -> emit(State.Error(data=null, status=TopicStatus.PARAMETER_MISSED))
            else -> {
                val topic = topicDao.getById(id)
                if (topic == null) {
                    emit(State.Error(data=null, status=TopicStatus.TOPIC_NOT_FOUND))
                } else {
                    emit(State.Success(data=topic, status=TopicStatus.SUCCESS))
                }
            }

        }


    }

    suspend fun disconnect() {

    }

    suspend fun sendMessage() {

    }
}