package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData

@Serializable
data class Chat(
    val id: Long,
    val title: String?,
    val members: List<Long>,
    val messages: List<Long>,
    val attachments: List<FileData>,
    val avatar: List<FileData> = listOf(),
    val creationTimestamp: Long
) {
    fun asResponseData(): ChatResponseData = ChatResponseData(
        id=id,
        title=title,
        members=members,
        messages= listOf(),
        attachments=attachments,
        avatar=avatar,
        creationTimestamp=creationTimestamp
    )
}