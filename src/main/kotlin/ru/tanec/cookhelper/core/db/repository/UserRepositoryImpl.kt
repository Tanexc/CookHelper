package ru.tanec.cookhelper.core.db.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.userDao.UserDao
import ru.tanec.cookhelper.core.db.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.domain.model.User
import ru.tanec.cookhelper.domain.repository.UserRepository

class UserRepositoryImpl(
    override val dao: UserDao = UserDaoImpl()
): UserRepository {
    override fun register(
        name: String,
        surname: String,
        nickname: String,
        email: String,
        password: String
    ): Flow<State<User?>> = flow {
        emit(State.Processing())

        try {

            //TODO reg

        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }

    }

    override fun login(login: String, password: String): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun setAvatar(avatar: String): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun action(): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun getUser(token: String, id: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun getAll(): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun changePassword(token: String, password: String): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun recoverAccess(code: String, email: String?, nickname: String?): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun recoveryCode(email: String?, nickname: String?): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun verify(code: String, email: String): Flow<State<User?>> {
        TODO("Not yet implemented")
    }
}