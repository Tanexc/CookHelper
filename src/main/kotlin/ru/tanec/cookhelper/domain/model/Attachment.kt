package ru.tanec.cookhelper.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: Int,
    val title: String,
    val type: String,
    val data: ByteArray
)
