package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.ChatData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

fun List<Any?>.allNotNull() = !this.contains(null)

suspend fun List<PartData>.getChatOrNull(repo: UserRepository): Chat? {
    val data = ChatData()
    var user: User? = null
    this.forEach { part ->
        println(if (part is PartData.FormItem) part.value else null)
        when (part.name) {
            "title" -> data.title = if (part is PartData.FormItem) part.value.filter{it!='"'} else null
            "token" -> user = if (part is PartData.FormItem) checkUserToken(repo, part.value.filter{it!='"'}) else null
            "members" -> data.members =
                if (part is PartData.FormItem) part.value.filter{it!='"'}.split("*").mapNotNull { it.toLongOrNull() }
                    .toList() else emptyList()

            "avatar" -> {
                if (part is PartData.FileItem) {
                    val file = FileController.uploadFile(chatDataFolder, part)
                    data.avatar = listOf(file)
                }
            }
        }
    }
    println("COCK $user")
    println("DATA COCK $data")
    return if (user == null) null else data.asDomain()
}

fun <T> List<T>.partOfDiv(part: Int?, div: Int?): List<T> {
    if (part == null || div == null) {
        return this
    }
    return when {
        ((div * (part + 1) > this.size) and (div * part < this.size)) -> {
            this.subList(div * part, this.size)
        }

        (div * (part + 1) <= this.size) -> {
            this.subList(div * part, div * (part + 1))
        }

        else -> emptyList()
    }

}

fun <T> List<T>.getPage(limit: Int, offset: Int): List<T> {
    return when(limit * (offset + 1) >= this.size) {
        true -> this.subList(limit * offset, this.size)
        else -> {
            this.subList(limit * offset, limit * (offset + 1))
        }
    }
}