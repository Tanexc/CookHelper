package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.MessageResponseData

@Serializable
data class Message(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val views: List<Long>
) {
    fun asResponseData(author: User): MessageResponseData = MessageResponseData(
        id=id,
        author=author.smallInfo(),
        text=text,
        attachments = attachments,
        replyToId = replyToId,
        timestamp = timestamp,
        views=views
    )
}