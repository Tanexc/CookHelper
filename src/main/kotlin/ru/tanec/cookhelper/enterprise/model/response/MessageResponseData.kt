package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData


@Serializable
data class MessageResponseData(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val name: String,
    val nickname: String,
    val surname: String,
    val avatar: String
)