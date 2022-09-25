package ru.tanec.cookhelper.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ChatReceiveObject(
    val text: String,
    val attachment: MutableList<Attachment>
)