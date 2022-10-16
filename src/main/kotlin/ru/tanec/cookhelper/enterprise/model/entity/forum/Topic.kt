package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.Attachment

@Serializable
data class Topic(
    val id: Long,
    val title: String,
    val problem: Answer,
    val answers: List<Answer>,
    val attachments: List<Attachment>,
    val timestamp: Long,
    val closed: Boolean
)