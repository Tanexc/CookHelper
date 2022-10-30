package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply
import ru.tanec.cookhelper.enterprise.model.entity.user.User

@Serializable
data class ReplyResponseData(
    val id: Long,
    val author: User,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val ratingPositive: List<Long>,
    val ratingNegative: List<Long>,
    val replies: List<Reply>
)