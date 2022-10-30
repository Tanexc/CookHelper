package ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.forum.Reply

@Serializable
data class ForumReceiveReplyData(
    val text: String,
    val attachment: List<FileData>,
    val replyTo: Long
) {
    fun asDomain(
        authorId: Long,
        ratingPositive: List<Long>,
        ratingNegative: List<Long>,
        timestamp: Long
    ): Reply = Reply(
        id = 0,
        text = text,
        attachments = attachment,
        authorId = authorId,
        replyToId = replyTo,
        timestamp = timestamp,
        ratingPositive = ratingPositive,
        ratingNegative = ratingNegative,
        replies = emptyList()

    )
}