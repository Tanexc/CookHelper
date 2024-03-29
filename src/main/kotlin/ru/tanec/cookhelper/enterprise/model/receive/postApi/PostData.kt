package ru.tanec.cookhelper.enterprise.model.receive.postApi

import io.ktor.http.content.*

data class PostData(
    val authorId: Long?,
    val text: String? = "",
    val label: String? = "",
    val attachment: List<PartData.FileItem>
)