package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

@Serializable
data class Message(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long
)