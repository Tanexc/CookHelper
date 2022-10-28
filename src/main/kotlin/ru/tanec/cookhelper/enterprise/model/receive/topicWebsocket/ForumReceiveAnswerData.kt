package ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.forum.Answer

@Serializable
data class ForumReceiveAnswerData(
    val text: String,
    val attachment: List<FileData>,
    val replyTo: Long
) {
    fun asDomain(
        authorId: Long,
        likes: List<Long>,
        timestamp: Long
    ): Answer = Answer(
        id = 0,
        text = text,
        attachments = attachment,
        authorId = authorId,
        likes = likes,
        replyToId = replyTo,
        timestamp = timestamp

    )
}