package ru.tanec.cookhelper.enterprise.model.receive.userApi

import io.ktor.util.date.*
import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.core.utils.HashTool
import ru.tanec.cookhelper.enterprise.model.entity.user.User

@Serializable
data class RegistrationData(
    var name: String? = null,
    var surname: String? = null,
    var nickname: String? = null,
    var email: String? = null,
    var password: String? = null
) {
    fun asDomain(): User = User(
        name=this.name?:"",
        surname=this.surname?:"",
        nickname=this.nickname?:"",
        email=this.email?:"",
        password=HashTool.getHash(this.password?:""),
        lastSeen = getTimeMillis()
    )
}