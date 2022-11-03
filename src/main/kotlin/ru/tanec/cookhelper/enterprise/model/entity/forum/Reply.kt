package ru.tanec.cookhelper.enterprise.model.entity.forum

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ReplyResponseData

@Serializable
data class Reply(
    val id: Long,
    val text: String,
    val attachments: List<FileData>,
    val replyToId: Long,
    val timestamp: Long,
    val ratingPositive: List<Long>,
    val ratingNegative: List<Long>,
    val authorId: Long,
    val replies: List<Long>
) {
    fun asResponseData(user: User?, repliesList: List<Reply>): ReplyResponseData? {
        if (user == null) return null
        return ReplyResponseData(
            id=id,
            text=text,
            attachments=attachments,
            replyToId=replyToId,
            timestamp=timestamp,
            ratingNegative=ratingNegative,
            ratingPositive=ratingPositive,
            author=user?.smallInfo(),
            replies = repliesList
        )
    }
}