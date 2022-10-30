package ru.tanec.cookhelper.enterprise.model.entity.chat

import io.ktor.util.date.*
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

data class ChatData(
    var title: String? = null,
    var avatar: List<FileData> = emptyList(),
    var members: List<Long>? = null
    ) {

    fun asDomain(): Chat? {

        return if (members != null) Chat(
            id = 0,
            title = title,
            avatar = avatar,
            members = members ?: emptyList(),
            messages = emptyList(),
            attachments = emptyList(),
            creationTimestamp = getTimeMillis()
        ) else null
    }

}