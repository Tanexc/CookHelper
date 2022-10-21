package ru.tanec.cookhelper.enterprise.model.entity.attachment

import kotlinx.serialization.Serializable

@Serializable
data class FileData(
    val id: String,
    val link: String
)
