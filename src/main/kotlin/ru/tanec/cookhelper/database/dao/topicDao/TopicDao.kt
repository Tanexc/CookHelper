package ru.tanec.cookhelper.database.dao.topicDao

import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

interface TopicDao {

    fun getById(id: Long): Topic?

    fun getByTitle(title: String): List<Topic>

    fun getByProblem(problem: String): List<Topic>

    fun insert(topic: Topic): Topic?

    fun editTopic(topic: Topic): Topic?

}