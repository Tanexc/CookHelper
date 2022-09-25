package ru.tanec.cookhelper.domain.model

data class ChatResponseObject(
    val message: String,
    val status: Int,
    val data: Message
)
