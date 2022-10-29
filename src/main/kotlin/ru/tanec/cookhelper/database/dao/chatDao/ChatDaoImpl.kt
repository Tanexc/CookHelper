package ru.tanec.cookhelper.database.dao.chatDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.ATTCH_DELIMITER
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.core.utils.FileController.toFileData
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Chats
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat

class ChatDaoImpl: ChatDao {

    private fun resultRowToChat(row: ResultRow): Chat = Chat(
        id=row[Chats.id],
        title= row[Chats.title].ifEmpty { null },
        members=row[Chats.members].split(" ").mapNotNull { it.toLongOrNull() },
        attachments=row[Chats.attachments].split(ATTCH_DELIMITER).mapNotNull{ if (it !="") it.toFileData(chatDataFolder) else null},
        avatar=row[Chats.avatar].split(FILE_DELIMITER).map {it.toFileData(chatDataFolder)},
        creationTimestamp=row[Chats.creationTimestamp],
        messages=row[Chats.messages].split(" ").mapNotNull {it.toLongOrNull()}
    )

    override suspend fun insert(chat: Chat): Chat? = dbQuery {
        Chats
            .insert{ row ->
                row[members] = chat.members.joinToString(" ")
                row[attachments] = chat.attachments.joinToString(ATTCH_DELIMITER) { it.id }
                row[messages] = chat.messages.joinToString(" ")
                row[avatar] = chat.avatar.joinToString(FILE_DELIMITER) { it.id }
                row[creationTimestamp] = chat.creationTimestamp
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

    override suspend fun edit(chat: Chat): Chat? = dbQuery {
        Chats
            .update { row ->
                row[messages] = chat.messages.joinToString(" ")
                row[title] = chat.title?: ""
                row[attachments] = chat.attachments.joinToString(ATTCH_DELIMITER) { it.id }
                row[avatar] = chat.avatar.joinToString(FILE_DELIMITER) { it.id }
            }

        Chats
            .select {Chats.id eq chat.id}
            .map(::resultRowToChat)
            .singleOrNull()
    }
}