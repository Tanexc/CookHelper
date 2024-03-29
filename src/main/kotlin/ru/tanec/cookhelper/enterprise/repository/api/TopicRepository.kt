package ru.tanec.cookhelper.enterprise.repository.api


import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

interface TopicRepository {

    val dao: TopicDao
    val replyDao: ReplyDao

    fun insert(topic: Topic): Flow<State<Topic?>>

    fun getByProblem(problem: String): Flow<State<List<Topic>>>

    fun getByTitle(title: String): Flow<State<List<Topic>>>

    fun editTopic(topic: Topic): Flow<State<Topic?>>

    fun getByListId(listId: List<Long>): Flow<State<List<Topic>?>>

    suspend fun getTopicReplies(id: Long, offset: Int, limit: Int): List<Reply>

    suspend fun getReplies(ids: List<Long>): List<Reply>

    suspend fun getTopicList(
        queryString: String,
        noRepliesFilter: Boolean,
        tagFilter: List<String>,
        imageFilter: Boolean,
        ratingNeutralFilter: Boolean,
        ratingSort: Boolean,
        reverseSort: Boolean,
        ratingPositiveFilter: Boolean,
        ratingNegativeFilter: Boolean,
        recencySort: Boolean,
        offset: Int?,
        limit: Int?
    ): Flow<State<List<Topic>?>>
}