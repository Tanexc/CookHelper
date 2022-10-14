package ru.tanec.cookhelper.enterprise.model.entity

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: Long = 0,
    val title: String
)