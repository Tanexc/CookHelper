package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDaoImpl
import ru.tanec.cookhelper.database.model.Topics
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository

class TopicRepositoryImpl(override val dao: TopicDao = TopicDaoImpl()) : TopicRepository {
    override fun insert(topic: Topic): Flow<State<Topic?>> = flow {
        try {
            emit(State.Processing())
            emit(
                when (val data = dao.insert(topic)) {
                    null -> State.Error(status = TOPIC_NOT_CREATED)
                    else -> State.Success(status = SUCCESS, data = data)
                }
            )
        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION, message = e.message ?: "Exception in insert() of TopicRepository"))
        }
    }

    override fun getByProblem(problem: String): Flow<State<List<Topic>>> = flow {
        try {
            emit(State.Processing())
            emit(State.Success(status = SUCCESS, data = dao.getByProblem(problem)))
        } catch (e: Exception) {
            emit(
                State.Error(
                    status = EXCEPTION,
                    message = e.message ?: "Exception in getByProblem() of TopicRepository"
                )
            )
        }
    }

    override fun getByTitle(title: String): Flow<State<List<Topic>>> = flow {
        try {
            emit(State.Processing())
            emit(State.Success(status = SUCCESS, data = dao.getByTitle(title)))
        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION, message = e.message ?: "Exception in getByTitle() of TopicRepository"))
        }
    }

    override fun editTopic(topic: Topic): Flow<State<Topic?>> = flow {
        try {
            emit(State.Processing())
            emit(
                when (val data = dao.editTopic(topic)) {
                    null -> State.Error(status = TOPIC_NOT_FOUND)
                    else -> State.Success(data = data)
                }
            )
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: "Exception in getByTitle() of TopicRepository"))
        }
    }

    override fun getByListId(listId: List<Long>): Flow<State<List<Topic>?>> = flow {
        try {
            emit(State.Processing())
            val data = mutableListOf<Topic>()
            for (id in listId) {
                dao.getById(id)?.let { data.add(it) }
            }
            emit(State.Success(data=data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: "Exception in getByListId() of TopicRepository"))
        }
    }

}