package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDao
import ru.tanec.cookhelper.database.dao.answerDao.ReplyDaoImpl
import ru.tanec.cookhelper.database.dao.topicDao.TopicDao
import ru.tanec.cookhelper.database.dao.topicDao.TopicDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.repository.api.TopicRepository

class TopicRepositoryImpl(
    override val dao: TopicDao = TopicDaoImpl(),
    override val replyDao: ReplyDao = ReplyDaoImpl()
) : TopicRepository {
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
            emit(State.Success(status = SUCCESS, data = dao.getByText(problem)))
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
            emit(State.Success(data = data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: "Exception in getByListId() of TopicRepository"))
        }
    }

    override suspend fun getTopicReplies(id: Long, offset: Int, limit: Int): List<Reply> =
        replyDao.getByListId(dao.getTopicMessages(id, offset, limit) ?: emptyList())

    override suspend fun getReplies(ids: List<Long>): List<Reply> = replyDao.getByListId(ids)
    override suspend fun getTopicList(
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
    ): Flow<State<List<Topic>?>> = flow {
        try {
            var data = dao.getTopicList(
                queryString,
                noRepliesFilter,
                tagFilter,
                imageFilter,
                ratingNeutralFilter,
                ratingPositiveFilter,
                ratingNegativeFilter
            )
            data = if (ratingSort and !reverseSort) data.sortedBy { it.ratingPositive.size } else data.sortedBy { it.ratingNegative.size }
            data = if (recencySort and !reverseSort) data.sortedBy { it.timestamp } else data.sortedBy { it.timestamp }.reversed()

            data = data.getPage(limit, offset)

            emit(State.Success(data = data))
        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION, message = "error in getTopicList of TopicRepository. Message: ${e.message}"))
        }
    }


}