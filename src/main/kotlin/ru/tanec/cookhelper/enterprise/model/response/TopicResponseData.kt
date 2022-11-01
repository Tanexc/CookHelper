package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

@Serializable
data class TopicResponseData(
    val id: Long,
    val authorId: Long,
    val title: String,
    val problem: String,
    val replies: List<ReplyResponseData>,
    val attachments: List<FileData>,
    val timestamp: Long,
    val closed: Boolean
) {
}