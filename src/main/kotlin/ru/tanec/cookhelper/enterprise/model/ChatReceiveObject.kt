package ru.tanec.cookhelper.enterprise.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatReceiveObject(
    val text: String,
    val attachment: MutableList<Attachment>
)