package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.forum.Answer
import ru.tanec.cookhelper.enterprise.model.entity.user.User

@Serializable
data class AnswerResponseData(
    val id: Long,
    val authorId: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val likes: List<Long>,
    val name: String,
    val nickname: String,
    val surname: String,
    val avatar: FileData
)