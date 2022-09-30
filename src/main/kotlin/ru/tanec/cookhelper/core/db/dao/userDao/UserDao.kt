package ru.tanec.cookhelper.core.db.dao.userDao

import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.domain.model.User

interface UserDao {
    suspend fun getAll(): MutableList<User>
    suspend fun getById(id: Long): User?
    suspend fun addNew(user: User): User?
    suspend fun editById(id: Long, user: User): User?
    suspend fun deleteById(id: Long): Boolean
}