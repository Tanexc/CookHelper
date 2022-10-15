package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.chat.Attachment

@Serializable
data class Topic(
    val id: Long,
    val title: String,
    val answers: List<Answer>,
    val attachments: List<Attachment>
)