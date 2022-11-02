package ru.tanec.cookhelper.database.dao.answerDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.partOfDiv
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Replies
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply

class ReplyDaoImpl: ReplyDao {

    private fun resultRowToAnswer(row: ResultRow) = Reply(
        id = row[Replies.id],
        text=row[Replies.text],
        authorId =row[Replies.authorId],
        replyToId=row[Replies.replyToId],
        attachments = row[Replies.attachments].split(ATTCH_DELIMITER).mapNotNull{toFileData(it)},
        timestamp = row[Replies.timestamp],
        replies = row[Replies.replies].split(" ").mapNotNull{ it.toLongOrNull()},
        ratingPositive = row[Replies.ratingPositive].split(" ").mapNotNull{ it.toLongOrNull()},
        ratingNegative = row[Replies.ratingNegative].split(" ").mapNotNull{ it.toLongOrNull()}
    )

    override suspend fun getById(id: Long): Reply? = dbQuery {
        Replies
            .select {Replies.id eq id}
            .map(::resultRowToAnswer)
            .singleOrNull()
    }

    override suspend fun getByListId(listId: List<Long>): List<Reply> {
        val data = mutableListOf<Reply>()
        var item: Reply?
        for (id in listId) {
            item = getById(id)
            if (item != null) {
                data.add(item)
            }
        }
        return data.toList()
    }

    override suspend fun getByListId(listId: List<Long>, part: Int, div: Int): List<Reply> {
        val data = mutableListOf<Reply>()
        var item: Reply?
        for (id in listId) {
            item = getById(id)
            if (item != null) {
                data.add(item)
            }
        }
        return data.toList().partOfDiv(part, div)
    }

    override suspend fun insert(reply: Reply): Reply? = dbQuery {
        Replies
            .insert{ row ->
                row[authorId] = reply.authorId
                row[text] = reply.text
                row[attachments] = reply.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[replyToId] = reply.replyToId
                row[timestamp] = reply.timestamp
                row[replies] = reply.replies.joinToString(" ")
                row[ratingNegative] = reply.ratingNegative.joinToString(" ")
                row[ratingPositive] = reply.ratingPositive.joinToString(" ")
            }
        Replies
            .select {(Replies.timestamp eq reply.timestamp) and (Replies.authorId eq reply.authorId)}
            .map(::resultRowToAnswer)
            .singleOrNull()
    }

    override suspend fun edit(reply: Reply): Reply? = dbQuery {
        Replies
            .update({ Replies.id eq reply.id }){ row ->
                row[text] = reply.text
                row[attachments] = reply.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[replies] = reply.replies.joinToString(" ")
                row[ratingNegative] = reply.ratingNegative.joinToString(" ")
                row[ratingPositive] = reply.ratingPositive.joinToString(" ")
            }
        Replies
            .select {Replies.id eq reply.id}
            .map(::resultRowToAnswer)
            .singleOrNull()
    }
}