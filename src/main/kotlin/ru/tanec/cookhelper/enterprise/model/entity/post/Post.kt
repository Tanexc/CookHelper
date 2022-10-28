package ru.tanec.cookhelper.enterprise.model.entity.post

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

@Serializable
data class Post(
    val id: Long = 0,
    val authorId: Long?,
    val text: String = "",
    val label: String = "",
    val attachments: List<FileData> = listOf(),

    val likes: List<Long>,
    val comments: List<String>,
    val reposts: List<Long>,

    val timestamp: Long
) {


}
