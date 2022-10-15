package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val sender: Long,
    val text: String,
    val attachment: List<Attachment>?,
    val replyTo: Long,
    val timestamp: Long
)