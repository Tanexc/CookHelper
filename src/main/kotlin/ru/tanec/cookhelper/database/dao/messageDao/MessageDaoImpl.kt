package ru.tanec.cookhelper.database.dao.messageDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.attachmentDataFloder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
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
        attachments=row[Messages.attachments].split(ATTCH_DELIMITER).mapNotNull{ toFileData(it)},
        replyToId=row[Messages.replyToId],
        timestamp=row[Messages.timestamp],
        views = row[Messages.views].split(" ").mapNotNull { it.toLongOrNull() }

    )

    private fun ResultRow.toMessage(): Message = Message(
        id=this[Messages.id],
        authorId=this[Messages.authorId],
        text=this[Messages.text],
        attachments=this[Messages.attachments].split(ATTCH_DELIMITER).mapNotNull{ toFileData(it)},
        replyToId=this[Messages.replyToId],
        timestamp=this[Messages.timestamp],
        views = this[Messages.views].split(" ").mapNotNull { it.toLongOrNull() }
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
                row[attachments] = message.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[replyToId] = message.replyToId
                row[timestamp] = message.timestamp
                row[views] = message.views.joinToString(" ")
            }.resultedValues?.get(0)

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

    override suspend fun edit(message: Message) = dbQuery {
        Messages
            .update({ Messages.id eq message.id }) { row ->
                row[views] = message.views.joinToString(" ")
                row[text] = message.text
                row[attachments] = message.attachments.joinToString(ATTCH_DELIMITER) { it.name }
            }
    }
}