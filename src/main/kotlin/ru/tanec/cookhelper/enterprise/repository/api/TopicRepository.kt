package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

interface TopicRepository {

    val dao: TopicDao

    fun insert(topic: Topic): Flow<State<Topic?>>

    fun getByProblem(problem: String): Flow<State<List<Topic>>>

    fun getByTitle(title: String): Flow<State<List<Topic>>>

    fun editTopic(topic: Topic): Flow<State<Topic?>>

    fun getByListId(listId: List<Long>): Flow<State<List<Topic>?>>
}