package ru.tanec.cookhelper.enterprise.model

data class ChatResponseObject(
    val message: String,
    val status: Int,
    val data: Message
)
