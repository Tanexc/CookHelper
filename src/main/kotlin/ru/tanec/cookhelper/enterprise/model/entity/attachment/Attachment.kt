package ru.tanec.cookhelper.enterprise.model.entity.attachment

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: Long,
    val file: String
)
