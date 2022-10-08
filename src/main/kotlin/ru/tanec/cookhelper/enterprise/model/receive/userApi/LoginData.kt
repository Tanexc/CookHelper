package ru.tanec.cookhelper.enterprise.model.receive.userApi

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class LoginData(
    val login: String?,
    val password: String?
) {
    fun toData(params: Parameters) =
        LoginData(
            login = params["login"],
            password = params["password"]
        )
}