package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData
import ru.tanec.cookhelper.enterprise.model.response.MessageResponseData

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
    fun asResponseData(
        member: User,
        lastMessage: Message?,
        newMessagesCount: Int,
        messages: List<MessageResponseData>
    ): ChatResponseData = ChatResponseData(
        id = id,
        title = title ?: "${member.name} ${member.surname}",
        members = members,
        messages = messages,
        images = avatar,
        creationTimestamp = creationTimestamp,
        lastMessage = lastMessage,
        newMessagesCount = newMessagesCount,
    )
}