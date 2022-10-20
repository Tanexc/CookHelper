package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.Attachment

@Serializable
data class Answer(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<Long>,
    val replyToId: Long,
    val timestamp: Long,
    val likes: List<Long>
)