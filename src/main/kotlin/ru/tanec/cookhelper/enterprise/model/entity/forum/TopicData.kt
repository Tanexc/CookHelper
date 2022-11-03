package ru.tanec.cookhelper.enterprise.model.entity.forum

import io.ktor.util.date.*
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

data class TopicData(
    var authorId:Long? = null,
    var title: String? = null,
    var problem: String? = null,
    var attachment: List<FileData> = emptyList(),
    var tags: List<String> = emptyList()

    ) {
    fun asDomain(): Topic = Topic(
        id=0,
        authorId=authorId?: 0,
        title=title?:"",
        text=problem?: "",
        replies= emptyList(),
        attachments = attachment,
        timestamp = getTimeMillis(),
        closed=false,
        tags = tags,
        ratingPositive = emptyList(),
        ratingNegative = emptyList()
    )

    fun equipped(): Boolean {
        return (authorId != null) and (title != null) and (problem != null)
    }
}