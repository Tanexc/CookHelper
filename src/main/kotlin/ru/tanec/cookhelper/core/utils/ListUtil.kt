package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.ChatData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

fun allNotNull(p: List<Any?>) = !p.contains(null)

suspend fun List<PartData>.getChatOrNull(repo: UserRepository): Chat? {
    val data = ChatData()
    var user: User? = null
    this.forEach { part ->
        when (part.name) {
            "token" -> user = if (part is PartData.FormItem) checkUserToken(repo, part.value.filter{it!='"'}) else null
            "members" -> data.members =
                if (part is PartData.FormItem) part.value.filter{it!='"'}.split("*").mapNotNull { it.toLongOrNull() }
                    .toList() else emptyList()

            "avatar" -> {
                if (part is PartData.FileItem) {
                    val file = FileController.uploadFile(chatDataFolder, part)
                    data.avatar = listOf(file.id)
                }
            }
        }
    }
    return if (user == null) null else data.asDomain()
}
