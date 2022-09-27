package ru.tanec.cookhelper.domain.repository

import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.domain.model.User

interface UserRepository {

    fun register(
        name: String,
        surname: String,
        nickname: String,
        email: String,
        password: String
    ): State<User?>

    fun login(
        login: String,
        password: String
    ): State<User?>

    fun setAvatar(
        avatar: String
    ): State<User?>

    fun action(): State<User?>

    //TODO: other functions


}