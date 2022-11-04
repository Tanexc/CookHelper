package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.enterprise.model.entity.user.User

@Serializable
data class ChatResponseData(
    val id: Long,
    val attachments: List<FileData>,
    val title: String?,
    val lastMessage: Message?,
    val newMessagesCount: Int,
    val members: List<Long>,
    val messages: List<MessageResponseData>,
    val creationTimestamp: Long,
    val avatar: List<FileData>,
    val member: User
)
