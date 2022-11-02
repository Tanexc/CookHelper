package ru.tanec.cookhelper.database.dao.topicDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.forumDataFolder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Topics
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

class TopicDaoImpl: TopicDao {

    private fun resultRowToTopic(row: ResultRow) = Topic(
        id = row[Topics.id],
        authorId = row[Topics.authorId],
        title= row[Topics.title],
        problem = row[Topics.problem],
        answers = row[Topics.answers].split(" ").mapNotNull { it.toLongOrNull()},
        attachments = row[Topics.attachments].split(ATTCH_DELIMITER).mapNotNull{toFileData(it)},
        timestamp = row[Topics.timestamp],
        closed = row[Topics.closed]
    )

    private fun ResultRow.toTopic() = Topic(
        id = this[Topics.id],
        authorId = this[Topics.authorId],
        title= this[Topics.title],
        problem = this[Topics.problem],
        answers = this[Topics.answers].split(" ").mapNotNull { it.toLongOrNull()},
        attachments = this[Topics.attachments].split(ATTCH_DELIMITER).mapNotNull{toFileData(it)},
        timestamp = this[Topics.timestamp],
        closed = this[Topics.closed]
    )

    override suspend fun getById(id: Long): Topic? = dbQuery {
        Topics
            .select { Topics.id eq id}
            .map(::resultRowToTopic)
            .singleOrNull()

    }

    override suspend fun getByTitle(title: String): List<Topic> = dbQuery {
        Topics
            .select { Topics.title like title}
            .map(::resultRowToTopic)
    }

    override suspend fun getByProblem(problem: String): List<Topic> = dbQuery {
        Topics
            .select { Topics.problem like problem}
            .map(::resultRowToTopic)
    }

    override suspend fun insert(topic: Topic): Topic? = dbQuery {
        Topics
            .insert { row ->
                row[authorId] = topic.authorId
                row[problem] = topic.problem
                row[title] = topic.title
                row[answers] = topic.answers.joinToString(" ")
                row[timestamp] = topic.timestamp
                row[attachments] = topic.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[closed] = topic.closed
            }.resultedValues?.get(0)?.toTopic()

        Topics
            .select { (Topics.problem eq topic.problem) and (Topics.timestamp eq topic.timestamp)}
            .map(::resultRowToTopic)
            .singleOrNull()
    }

    override suspend fun editTopic(topic: Topic): Topic? = dbQuery {
        Topics
            .update {row ->
                row[answers] = topic.answers.joinToString(" ")
                row[attachments] = topic.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[closed] = topic.closed
            }

        Topics
            .select {Topics.id eq topic.id}
            .map(::resultRowToTopic)
            .singleOrNull()
    }

    override suspend fun getTopicMessages(id: Long, offset: Int, limit: Int): List<Long>? = dbQuery {
        Topics
            .select{Topics.id eq id}
            .map(::resultRowToTopic)
            .singleOrNull()?.answers?.getPage(limit, offset)
    }
}