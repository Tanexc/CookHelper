package ru.tanec.cookhelper.enterprise.model.response_data

import ru.tanec.cookhelper.enterprise.model.entity_data.Message

data class ChatResponseData(
    val message: String,
    val status: Int,
    val data: Message
)
