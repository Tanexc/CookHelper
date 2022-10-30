package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

@Serializable
data class Reply(
    val id: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val ratingPositive: List<Long>,
    val ratingNegative: List<Long>,
    val authorId: Long,
    val replies: List<Long>
)