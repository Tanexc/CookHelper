package ru.tanec.cookhelper.enterprise.model.receive_data

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity_data.Attachment

@Serializable
data class ChatReceiveData(
    val text: String,
    val attachment: MutableList<Attachment>
)