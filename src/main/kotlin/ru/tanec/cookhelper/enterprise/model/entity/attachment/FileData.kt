package ru.tanec.cookhelper.enterprise.model.entity.attachment

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val id: Long,
    val name: String,
    val link: String,
    val type: String
)
