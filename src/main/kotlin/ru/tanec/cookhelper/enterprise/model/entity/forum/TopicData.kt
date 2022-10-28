package ru.tanec.cookhelper.enterprise.model.entity.forum

import io.ktor.util.date.*
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic

data class TopicData(
    var authorId:Long? = null,
    var title: String? = null,
    var problem: String? = null,
    var attachment: List<FileData> = emptyList(),

    ) {
    fun asDomain(): Topic = Topic(
        id=0,
        authorId=authorId?: 0,
        title=title?:"",
        problem=problem?: "",
        answers= emptyList(),
        attachments = attachment,
        timestamp = getTimeMillis(),
        closed=false
    )

    fun equipped(): Boolean {
        return (authorId != null) and (title != null) and (problem != null)
    }
}