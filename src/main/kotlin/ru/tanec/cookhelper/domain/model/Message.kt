package ru.tanec.cookhelper.domain.model

import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class Message(
    val id: Int,
    val sender: Int,
    val text: String,
    val attachment: MutableList<Attachment>?,
    val timestamp: Long
)