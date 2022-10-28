package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message

@Serializable
data class ChatResponseData(
    val id: Long,
    val title: String?,
    val members: List<Long>,
    val messages: List<Message>,
    val attachments: List<FileData>,
    val avatar: List<FileData> = emptyList(),
    val creationTimestamp: Long
)
