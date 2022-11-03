package ru.tanec.cookhelper.database.dao.topicDao

import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

interface TopicDao {

    suspend fun getById(id: Long): Topic?

    suspend fun getByTitle(title: String): List<Topic>

    suspend fun getByText(text: String): List<Topic>

    suspend fun insert(topic: Topic): Topic?

    suspend fun editTopic(topic: Topic): Topic?

    suspend fun getTopicMessages(id: Long, offset: Int, limit: Int): List<Long>?

    suspend fun getTopicList(
        queryString: String,
        noRepliesFilter: Boolean,
        tagFilter: List<String>,
        imageFilter: Boolean,
        ratingNeutralFilter: Boolean,
        ratingPositiveFilter: Boolean,
        ratingNegativeFilter: Boolean): List<Topic>

}