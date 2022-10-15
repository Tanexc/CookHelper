package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.chat.Attachment

@Serializable
data class Answer(
    val id: Long,
    val sender: Long,
    val text: String,
    val attachment: List<Attachment>?,
    val replyTo: Long,
    val timestamp: Long,
    val likes: List<Long>
)