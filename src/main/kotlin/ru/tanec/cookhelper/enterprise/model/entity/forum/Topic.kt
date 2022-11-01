package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.Attachment
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData

@Serializable
data class Topic(
    val id: Long,
    val authorId: Long,
    val title: String,
    val problem: String,
    val answers: List<Long>,
    val attachments: List<FileData>,
    val timestamp: Long,
    val closed: Boolean
) {
    fun asResponseData(): TopicResponseData = TopicResponseData(
        id= id,
        authorId=authorId,
        title=title,
        problem=problem,
        replies = emptyList(),
        attachments = attachments,
        timestamp=timestamp,
        closed=closed
    )
}