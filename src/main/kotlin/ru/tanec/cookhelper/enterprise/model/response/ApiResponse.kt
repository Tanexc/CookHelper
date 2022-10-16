package ru.tanec.cookhelper.enterprise.model.response

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.core.State

@Serializable
data class ApiResponse<T>(
    val status: Int,
    val message: String,
    val data: T?
) {
    fun <T> State<T>.fromState(): ApiResponse<T> = ApiResponse(this.status, this.message, this.data)
}