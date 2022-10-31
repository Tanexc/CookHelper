package ru.tanec.cookhelper.database.dao.messageDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.core.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.core.utils.partOfDiv
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Messages
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

class MessageDaoImpl: MessageDao {

    private fun resultRowToMessage(row: ResultRow): Message = Message(
        id=row[Messages.id],
        authorId=row[Messages.authorId],
        text=row[Messages.text],
        attachments=row[Messages.attachments].split(ATTCH_DELIMITER).mapNotNull{ if (it !="") it.toFileData(
            attachmentDataFloder
        ) else null},
        replyToId=row[Messages.replyToId],
        timestamp=row[Messages.timestamp]

    )

    override suspend fun getById(id: Long): Message? = dbQuery {
        Messages
            .select {Messages.id eq id}
            .map(::resultRowToMessage)
            .singleOrNull()
    }

    override suspend fun getByListId(listId: List<Long>): List<Message> = dbQuery {
        val data = mutableListOf<Message>()
        for (id in listId) {
            getById(id)?.let { data.add(it) }
        }
        data.toList()
    }

    override suspend fun getByListId(listId: List<Long>, part: Int?, div: Int?): List<Message> {
        val data = mutableListOf<Message>()
        for (id in listId) {
            getById(id)?.let { data.add(it) }
        }
        return data.toList().partOfDiv(part, div)
    }

    override suspend fun insert(message: Message): Message? = dbQuery {
        Messages
            .insert { row ->
                row[authorId] = message.authorId
                row[text] = message.text
                row[attachments] = message.attachments.joinToString(ATTCH_DELIMITER) { it.id }
                row[replyToId] = message.replyToId
                row[timestamp] = message.timestamp
            }

        Messages
            .select {(Messages.timestamp eq message.timestamp) and (Messages.authorId eq message.authorId)}
            .map(::resultRowToMessage)
            .singleOrNull()
    }

    override suspend fun getByOffset(listId: List<Long>, offset: Int, limit: Int): List<Message> {
        val data = mutableListOf<Message>()
        for (id in listId) {
            getById(id)?.let { data.add(it) }
        }
        return data.toList().getPage(limit, offset)
    }
}