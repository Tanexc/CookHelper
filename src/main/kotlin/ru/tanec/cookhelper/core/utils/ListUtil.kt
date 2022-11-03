package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.database.utils.FileController
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
                if ((part is PartData.FileItem) && (part.contentType != null)) {

                    val file = FileController.uploadFile(chatDataFolder, part, part.contentType!!.contentType)
                    data.avatar = listOf(file)
                }
            }
        }
    }

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

fun <T> List<T>.getPage(limit: Int?, offset: Int?): List<T> {
    if (limit == null || offset == null ) return this
    return when(limit * (offset + 1) >= this.size) {
        true -> {
            if (limit * offset < this.size) this.reversed().subList(limit * offset, this.size)
            else emptyList()
        }
        else -> {
            this.reversed().subList(limit * offset, limit * (offset + 1))
        }
    }
}

//TODO fun that checks percent of intersection

fun <T> List<T>.intersectionMoreThan(list: List<T>, fraction: Double): Boolean = (this.size / this.intersect(list.toSet()).size.toDouble()) > fraction

inline fun <T, R> List<T>.filterMap(predicate: (R) -> Boolean, transform: (T) -> R): List<R> {
    return this.mapNotNull { if (predicate(transform(it))) transform(it) else null }
}

fun <T> List<T>.without(list: List<T>): List<T> {
    val data = this.toMutableList()
    data.removeAll(list)
    return data.toList()
}