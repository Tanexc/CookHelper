package ru.tanec.cookhelper.enterprise.model.entity.comment

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val id: Long,
    val authorId: Long,
    val text: String,
    val timestamp: Long
)