package ru.tanec.cookhelper.enterprise.model.receive.chatWebsocket

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

@Serializable
data class ChatReceiveMessageData(
    val text: String,
    val attachment: List<FileData>,
    val replyTo: Long
) {
    fun asDomain(
        authorId: Long,
        timestamp: Long
    ): Message = Message(
        id = 0,
        text = text,
        attachments = attachment,
        authorId = authorId,
        replyToId = replyTo,
        timestamp = timestamp

    )
}