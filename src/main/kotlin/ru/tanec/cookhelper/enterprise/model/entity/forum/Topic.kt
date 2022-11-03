package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ReplyResponseData
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData

@Serializable
data class Topic(
    val id: Long,
    val authorId: Long,
    val title: String,
    val text: String,
    val replies: List<Long>,
    val attachments: List<FileData>,
    val tags: List<String>,
    val timestamp: Long,
    val closed: Boolean,
    val ratingPositive: List<Long>,
    val ratingNegative: List<Long>
) {
    fun asResponseData(author: User?, replies: List<ReplyResponseData>): TopicResponseData? {
        if (author == null) return null
        return TopicResponseData(
            id = id,
            author = author,
            title = title,
            text = text,
            replies = replies,
            attachments = attachments,
            timestamp = timestamp,
            closed = closed,
            tags = tags,
            ratingPositive = ratingPositive,
            ratingNegative = ratingNegative
        )
    }
}