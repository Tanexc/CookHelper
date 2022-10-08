package ru.tanec.cookhelper.enterprise.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Int,
    val sender: Int,
    val text: String,
    val attachment: MutableList<Attachment>?,
    val timestamp: Long
)