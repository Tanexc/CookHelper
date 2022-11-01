package ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

@Serializable
data class ChatReceiveMessageData(
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long
) {
    fun asDomain(
        authorId: Long,
        timestamp: Long
    ): Message = Message(
        id = 0,
        text = text,
        attachments = attachments,
        authorId = authorId,
        replyToId = replyToId,
        timestamp = timestamp,
        views = listOf(authorId)
    )
}