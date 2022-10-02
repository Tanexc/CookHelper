package ru.tanec.cookhelper.core.db.dao.userDao

import ru.tanec.cookhelper.domain.model.User

interface UserDao {
    suspend fun getAll(): MutableList<User>
    suspend fun getById(id: Long): User?
    suspend fun addNew(user: User): User?
    suspend fun editUser(user: User): User
    suspend fun deleteById(id: Long): Boolean

    suspend fun getByLogin(login: String): MutableList<User>
    suspend fun getByToken(token: String): User?
}