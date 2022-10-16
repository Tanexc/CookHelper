package ru.tanec.cookhelper.enterprise.model.receive

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.Attachment

@Serializable
data class ChatReceiveData(
    val text: String,
    val attachment: MutableList<Attachment>
)