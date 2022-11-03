package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.user.User

@Serializable
data class TopicResponseData(
    val id: Long,
    val author: User,
    val title: String,
    val text: String,
    val replies: List<ReplyResponseData>,
    val attachments: List<FileData>,
    val tags: List<String>,
    val timestamp: Long,
    val closed: Boolean,
    val ratingPositive: List<Long>,
    val ratingNegative: List<Long>
)