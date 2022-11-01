package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

@Serializable
data class ChatResponseData(
    val id: Long,
    val images: List<FileData>?,
    val title: String?,
    val lastMessage: Message?,
    val newMessagesCount: Int,
    val members: List<Long>,
    val messages: List<MessageResponseData>,
    val creationTimestamp: Long
)
