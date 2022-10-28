package ru.tanec.cookhelper.database.dao.answerDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.SECOND_DELIMITER
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.core.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.partOfDiv
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Answers
import ru.tanec.cookhelper.database.model.Topics
import ru.tanec.cookhelper.enterprise.model.entity.forum.Answer
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

class AnswerDaoImpl: AnswerDao {

    private fun resultRowToAnswer(row: ResultRow) = Answer(
        id = row[Answers.id],
        text=row[Answers.text],
        authorId =row[Answers.authorId],
        replyToId=row[Answers.replyToId],
        attachments = row[Answers.attachments].split(ATTCH_DELIMITER).map{it.toFileData(attachmentDataFloder)}.filter {it.id != ""},
        timestamp = row[Answers.timestamp],
        likes = row[Answers.likes].split(SECOND_DELIMITER).mapNotNull{it.toLongOrNull()}

    )

    override suspend fun getById(id: Long): Answer? = dbQuery {
        Answers
            .select {Answers.id eq id}
            .map(::resultRowToAnswer)
            .singleOrNull()
    }

    override suspend fun getByListId(listId: List<Long>): List<Answer> {
        val data = mutableListOf<Answer>()
        var item: Answer?
        for (id in listId) {
            item = getById(id)
            if (item != null) {
                data.add(item)
            }
        }
        return data.toList()
    }

    override suspend fun getByListId(listId: List<Long>, part: Int, div: Int): List<Answer> {
        val data = mutableListOf<Answer>()
        var item: Answer?
        for (id in listId) {
            item = getById(id)
            if (item != null) {
                data.add(item)
            }
        }
        return data.toList().partOfDiv(part, div)
    }

    override suspend fun insert(answer: Answer): Answer? = dbQuery {
        Answers
            .insert{ row ->
                row[authorId] = answer.authorId
                row[text] = answer.text
                row[attachments] = answer.attachments.joinToString(ATTCH_DELIMITER) { it.id }
                row[replyToId] = answer.replyToId
                row[timestamp] = answer.timestamp
                row[likes] = answer.likes.joinToString(SECOND_DELIMITER)
            }
        Answers
            .select {(Answers.timestamp eq answer.timestamp) and (Answers.authorId eq answer.authorId)}
            .map(::resultRowToAnswer)
            .singleOrNull()
    }
}