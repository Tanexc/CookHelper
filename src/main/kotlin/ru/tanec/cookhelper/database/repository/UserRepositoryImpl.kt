package ru.tanec.cookhelper.database.repository

import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.database.dao.userDao.UserDao
import ru.tanec.cookhelper.database.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.core.utils.HashTool
import ru.tanec.cookhelper.core.utils.JwtTool.generateToken
import ru.tanec.cookhelper.core.utils.ValidatorImpl
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.utls.Validator.*


class UserRepositoryImpl(
    override val dao: UserDao = UserDaoImpl(),
    override val hashTool: HashTool = HashTool,
    override val validator: ValidatorImpl = ValidatorImpl
) : UserRepository {

    override fun edit(user: User): Flow<State<User?>> = flow {
        try {

            val data = dao.editUser(user)
            emit(State.Success(data))

        } catch (e: Exception) {
            emit(State.Error(message=e.message?: "error in edit() of UserRepository"))
        }
    }

    override suspend fun getFullUser(user: User): Flow<User?> = flow {
        emit(dao.getFullUser(user))
    }

    override fun register(
        user: User
    ): Flow<State<User?>> = flow {
        emit(State.Processing())

        try {

            if (!nicknameAccessibility(user.nickname)) throw Exception("nickname rejected")
            if (!emailAccessibility(user.email)) throw Exception("login rejected")

            val timestamp = getTimeMillis()
            val token = generateToken(user.name, user.surname, timestamp, user.password.toString())

            val data = dao.addNew(user.copy(registrationTimestamp = timestamp, token = token))


            if (data != null) emit(State.Success(data))
            else emit(State.Error())
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"error in register of UserRepository"))
        }

    }

    override fun login(login: String, password: String): Flow<State<User?>> = flow {
        emit(State.Processing())

        var user = dao.getByLogin(login)

        if (user == null) emit(State.Error(status = USER_NOT_FOUND))
        else if (hashTool.verifyHash(password, user.password)) {
            if (user.token == "") user.token = generateToken(
                user.name,
                user.surname,
                user.registrationTimestamp ?: 0L,
                user.password.toString()
            )
            user = action(user)
            emit(State.Success(user, status = SUCCESS))
        } else emit(State.Error(status = WRONG_CREDENTIALS))

    }

    override fun addAvatar(
        token: String,
        avatarPath: String
    ): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = WRONG_CREDENTIALS))
        else {
            user.avatar = user.avatar + (toFileData(avatarPath)?.let{listOf(it)}?: emptyList())
            user = action(user)
            dao.editUser(user)
            emit(State.Success(user))
        }
    }

    override fun deleteAvatar(token: String, avatarId: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = WRONG_CREDENTIALS))
        else {

            user.avatar = user.avatar.filter { it.name != avatarId}
            user = action(user)
            dao.editUser(user)
            emit(State.Success(user))
        }
    }


    override fun getUser(token: String, id: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByToken(token)
        val requestedUser = dao.getById(id)
        if (user == null) emit(State.Expired(message = "token expired", status = TOKEN_EXPIRED))
        else if (requestedUser == null) emit(
            State.Error(
                message = "user not found",
                status = USER_NOT_FOUND
            )
        )
        else if (requestedUser.id == user.id) emit(
            State.Success(
                user.privateInfo(),
                status = SUCCESS
            )
        ) else emit(State.Success(requestedUser, status = SUCCESS))

    }

    override fun getAll(): Flow<State<List<User>>> = flow {
        emit(State.Processing())
        try {
            val userList = dao.getAll().map { it.smallInfo() }
            emit(State.Success(data = userList))
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"error in getAll() of UserRepository"))
        }
    }

    override fun getByNickname(nickname: String): Flow<State<List<User>?>> = flow {
        emit(State.Processing())
        try {
            val data = dao.getByNickname(nickname)
            emit(State.Success(data = data))
        } catch (e: Exception) {
            emit(State.Error(message=e.message?:"error in getAll() of UserRepository"))
        }
    }

    override fun recoverAccess(code: String, login: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(login)
        if (user == null) emit(State.Error(message = "user not found", status = USER_NOT_FOUND))
        else if (user.checkRecoveryCode(code)) emit(
            State.Success(
                user,
                status = SUCCESS
            )
        ) else emit(State.Error(message = "incorrect code", status = WRONG_CREDENTIALS))
    }

    override fun recoveryCode(login: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(login)
        if (user == null) emit(State.Error(message = "user not found", status = USER_NOT_FOUND))
        else {
            user.generateRecoveryCode()
            dao.editUser(user)
            emit(State.Success(user, status = SUCCESS))
        }
    }

    override fun verify(code: String, email: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(email)
        if (user == null) emit(State.Error(message = "user not found", status = USER_NOT_FOUND))
        else if (user.code == code) {
            dao.editUser(user); emit(State.Success(user, status = SUCCESS))
        } else emit(State.Error(message = "incorrect code", status = WRONG_CREDENTIALS))
    }

    override fun validatePassword(password: String): Boolean =
        when (validator.isValidLogin(password)) {
            is Validity.Valid -> true
            is Validity.Invalid -> false
        }

    override suspend fun nicknameAccessibility(nickname: String): Boolean =
        when (validator.isValidLogin(nickname)) {
            is Validity.Valid -> {
                dao.getByLogin(nickname) == null
            }
            is Validity.Invalid -> false
        }

    override suspend fun emailAccessibility(email: String): Boolean =
        when (validator.isValidLogin(email)) {
            is Validity.Valid -> {
                dao.getByLogin(email) == null
            }

            is Validity.Invalid -> false
        }

    override suspend fun action(user: User): User {
        user.lastSeen = getTimeMillis()
        dao.editUser(user)
        return user
    }

    override suspend fun getByToken(token: String): Flow<State<User?>> = flow {
        val user = dao.getByToken(token)
        println("user: $user")
        if (user != null) emit(State.Success(user))
        else emit(State.Error(status=USER_NOT_FOUND))
    }

    override fun getById(id: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        try {
            val user = dao.getById(id)
            emit(State.Success(data = user))
        } catch(e: Exception) {
            emit(State.Error(message=e.message?: "error in getById() in UserRepository"))
        }

    }

}