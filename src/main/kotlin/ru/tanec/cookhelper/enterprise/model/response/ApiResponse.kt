package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
)