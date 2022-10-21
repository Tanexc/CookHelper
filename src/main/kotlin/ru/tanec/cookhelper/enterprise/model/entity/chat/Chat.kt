package ru.tanec.cookhelper.enterprise.model.entity.chat

import kotlinx.serialization.Serializable

@Serializable
data class Chat(
    val id: Long,
    val members: List<Long>,
    val messages: List<Long>,
    val attachments: List<Long>,
    val avatar: List<String> = listOf(),
    val creationTimestamp: Long
)