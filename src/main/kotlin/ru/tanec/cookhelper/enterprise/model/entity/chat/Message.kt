package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.Attachment

@Serializable
data class Message(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<Attachment>,
    val replyToId: Long,
    val timestamp: Long
)