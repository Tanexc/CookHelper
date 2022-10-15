package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: Long,
    val file: String
)
