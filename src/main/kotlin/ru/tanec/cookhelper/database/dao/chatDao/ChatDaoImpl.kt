package ru.tanec.cookhelper.database.dao.chatDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Chats
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

class ChatDaoImpl : ChatDao {

    private fun resultRowToChat(row: ResultRow): Chat = Chat(
        id = row[Chats.id],
        title = row[Chats.title]?.ifEmpty { null },
        members = row[Chats.members].split(" ").mapNotNull { it.toLongOrNull() },
        attachments = row[Chats.attachments].split(ATTCH_DELIMITER).mapNotNull { toFileData(it) },
        avatar = row[Chats.avatar].split(FILE_DELIMITER).mapNotNull { toFileData(it) },
        creationTimestamp = row[Chats.creationTimestamp],
        messages = row[Chats.messages].split(" ").mapNotNull { it.toLongOrNull() }
    )

    override suspend fun insert(chat: Chat): Chat? = dbQuery {
        Chats
            .insert { row ->
                row[members] = chat.members.joinToString(" ")
                row[attachments] = chat.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[messages] = chat.messages.joinToString(" ")
                row[avatar] = chat.avatar.joinToString(FILE_DELIMITER) { it.name }
                row[creationTimestamp] = chat.creationTimestamp
            }

        Chats
            .select {
                (Chats.creationTimestamp eq chat.creationTimestamp) and (Chats.members eq chat.members.joinToString(
                    " "
                ))
            }
            .map(::resultRowToChat)
            .singleOrNull()

    }

    override suspend fun getById(id: Long): Chat? = dbQuery {
        Chats
            .select { Chats.id eq id }
            .map(::resultRowToChat)
            .singleOrNull()
    }

    override suspend fun getByList(listId: List<Long>, limit: Int?, offset: Int?): List<Chat> {
        val data = mutableListOf<Chat>()
        for (id in listId) {
            getById(id)?.let { data.add(it) }
        }
        return if (limit != null && offset != null) data.toList().getPage(limit, offset) else data.toList()
    }

    override suspend fun edit(chat: Chat): Chat? = dbQuery {
        Chats
            .update { row ->
                row[messages] = chat.messages.joinToString(" ")
                row[title] = chat.title ?: ""
                row[attachments] = chat.attachments.joinToString(ATTCH_DELIMITER) { it.name }
                row[avatar] = chat.avatar.joinToString(FILE_DELIMITER) { it.name }
            }

        Chats
            .select { Chats.id eq chat.id }
            .map(::resultRowToChat)
            .singleOrNull()
    }

    override suspend fun getChatMessages(id: Long, offset: Int?, limit: Int?): List<Long>? = dbQuery {
        Chats
            .select { Chats.id eq id }
            .map(::resultRowToChat)
            .singleOrNull()?.messages?.getPage(limit, offset)
    }
}