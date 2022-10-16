package ru.tanec.cookhelper.enterprise.model.entity.post

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long = 0,
    val authorId: Long?,
    val text: String = "",
    val attachment: List<String> = listOf(),
    val images: List<String> = listOf(),

    val likes: List<Long>,
    val comments: List<String>,
    val reposts: List<Long>,

    val timestamp: Long
) {


}
