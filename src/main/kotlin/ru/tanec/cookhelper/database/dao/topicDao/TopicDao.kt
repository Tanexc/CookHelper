package ru.tanec.cookhelper.database.dao.topicDao

import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

interface TopicDao {

    suspend fun getById(id: Long): Topic?

    suspend fun getByTitle(title: String): List<Topic>

    suspend fun getByProblem(problem: String): List<Topic>

    suspend fun insert(topic: Topic): Topic?

    suspend fun editTopic(topic: Topic): Topic?

}