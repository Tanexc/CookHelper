package ru.tanec.cookhelper.database.dao.chatDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Chats
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

class ChatDaoImpl: ChatDao {

    private fun resultRowToChat(row: ResultRow): Chat = Chat(
        id=row[Chats.id],
        members=row[Chats.members].split(" ").mapNotNull { it.toLongOrNull() },
        attachments=row[Chats.attachments].split(" ").mapNotNull { it.toLongOrNull() },
        avatar=row[Chats.avatar].split(FILE_DELIMITER),
        creationTimestamp=row[Chats.creationTimestamp],
        messages=row[Chats.messages].split(" ").mapNotNull {it.toLongOrNull()}
    )

    override suspend fun insert(chat: Chat): Chat? = dbQuery {
        Chats
            .insert{
                it[members] = chat.members.joinToString(" ")
                it[attachments] = chat.attachments.joinToString(" ")
                it[messages] = chat.messages.joinToString(" ")
                it[avatar] = chat.avatar.joinToString(FILE_DELIMITER)
                it[creationTimestamp] = chat.creationTimestamp
            }

        Chats
            .select {(Chats.creationTimestamp eq chat.creationTimestamp) and (Chats.members eq chat.members.joinToString(" "))}
            .map(::resultRowToChat)
            .singleOrNull()

    }

    override suspend fun getById(id: Long): Chat? = dbQuery {
        Chats
            .select { Chats.id eq id}
            .map(::resultRowToChat)
            .singleOrNull()
    }

    override suspend fun getByList(listId: List<Long>, part: Int?, div: Int?): List<Chat> {
        val data = mutableListOf<Chat>()
        for (id in listId) {
            getById(id)?.let {data.add(it)}
        }
        return data.toList()
    }
}