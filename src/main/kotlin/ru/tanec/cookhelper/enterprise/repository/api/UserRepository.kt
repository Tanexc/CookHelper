package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.core.utils.HashTool
import ru.tanec.cookhelper.core.utils.ValidatorImpl
import ru.tanec.cookhelper.enterprise.model.entity.user.User

interface UserRepository {
    val dao: UserDao
    val hashTool: HashTool
    val validator: ValidatorImpl

    fun edit(
        user: User
    ): Flow<State<User?>>
    fun register(
        user: User
    ): Flow<State<User?>>

    fun login(
        login: String,
        password: String
    ): Flow<State<User?>>

    fun addAvatar(
        token: String,
        avatarPath: String
    ): Flow<State<User?>>

    fun deleteAvatar(
        token: String,
        avatarId: String
    ): Flow<State<User?>>

    suspend fun getByToken(token: String): Flow<State<User?>>

    fun getUser(
        token: String,
        id: Long
    ): Flow<State<User?>>

    fun getAll(): Flow<State<List<User>>>

    fun getById(id: Long): Flow<State<User?>>

    fun getByNickname(
        nickname: String
    ): Flow<State<List<User>?>>

    fun recoverAccess(
        code: String,
        login: String
    ): Flow<State<User?>>


    fun recoveryCode(
        login: String
    ): Flow<State<User?>>

    fun verify(
        code: String,
        email: String
    ): Flow<State<User?>>

    fun validatePassword(
        password: String
    ): Boolean

    suspend fun emailAccessibility(
        email: String
    ): Boolean

    suspend fun nicknameAccessibility(
        nickname: String
    ): Boolean

    suspend fun action(user: User): User
    suspend fun getFullUser(user: User): Flow<User?>
}