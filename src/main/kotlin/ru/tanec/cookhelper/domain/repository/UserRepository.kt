package ru.tanec.cookhelper.domain.repository

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.userDao.UserDao
import ru.tanec.cookhelper.domain.model.User

interface UserRepository {

    val dao: UserDao
    fun register(
        name: String,
        surname: String,
        nickname: String,
        email: String,
        password: String
    ): Flow<State<User?>>

    fun login(
        login: String,
        password: String
    ): Flow<State<User?>>

    fun setAvatar(
        avatar: String
    ): Flow<State<User?>>

    fun action(): Flow<State<User?>>

    fun getUser(
        token: String,
        id: Long
        ): Flow<State<User?>>

    fun getAll(): Flow<State<User?>>

    fun changePassword(
        token: String,
        password: String
    ): Flow<State<User?>>

    fun recoverAccess(
        code: String,
        email: String?,
        nickname: String?
    ): Flow<State<User?>>


    fun recoveryCode(
        email: String?,
        nickname: String?
    ): Flow<State<User?>>

    fun verify(
        code: String,
        email: String
    ): Flow<State<User?>>

}