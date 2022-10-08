package ru.tanec.cookhelper.enterprise.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Attachment(
    val id: Int,
    val title: String,
    val type: String,
    val data: ByteArray
)
