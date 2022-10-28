package ru.tanec.cookhelper.database.dao.userDao

import ru.tanec.cookhelper.enterprise.model.entity.user.User

interface UserDao {
    suspend fun getAll(): List<User>
    suspend fun getById(id: Long): User?
    suspend fun addNew(user: User): User?
    suspend fun editUser(user: User): User
    suspend fun deleteById(id: Long): Boolean
    suspend fun getByLogin(login: String): User?
    suspend fun getByToken(token: String): User?
    suspend fun getByNickname(nickname: String): List<User>
}