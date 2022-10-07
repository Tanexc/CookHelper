package ru.tanec.cookhelper.enterprise.model.receive_data.userApi

import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class RegistrationData(
    val name: String?,
    val surname: String?,
    val nickname: String?,
    val email: String?,
    val password: String?
)