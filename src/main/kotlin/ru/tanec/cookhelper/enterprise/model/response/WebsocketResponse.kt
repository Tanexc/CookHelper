package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable

@Serializable
data class WebsocketResponse<T>(
    val status: Int,
    val data: T?
)