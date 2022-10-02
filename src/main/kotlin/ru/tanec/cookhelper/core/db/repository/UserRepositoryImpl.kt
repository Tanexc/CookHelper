package ru.tanec.cookhelper.core.db.repository

import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.userDao.UserDao
import ru.tanec.cookhelper.core.db.dao.userDao.UserDaoImpl
import ru.tanec.cookhelper.core.utils.HashTool
import ru.tanec.cookhelper.core.utils.ValidatorImpl
import ru.tanec.cookhelper.domain.model.User
import ru.tanec.cookhelper.domain.repository.UserRepository
import ru.tanec.cookhelper.domain.utls.Validator.*


class UserRepositoryImpl(
    override val dao: UserDao = UserDaoImpl(),
    override val hashTool: HashTool,
    override val validator: ValidatorImpl
) : UserRepository {
    override fun register(
        name: String,
        surname: String,
        nickname: String,
        email: String,
        password: String
    ): Flow<State<User?>> = flow {
        emit(State.Processing())

        try {

            if (!nicknameAccessibility(nickname)) throw Exception("nickname rejected")
            if (!emailAccessibility(email)) throw Exception("login rejected")
            if (!validatePassword(password)) throw Exception("invalid password")

            val user = dao.addNew(
                User(
                    id = 0,
                    name = name,
                    surname = surname,
                    nickname = nickname,
                    email = email,
                    password = hashTool.getHash(password),
                    registrationTimestamp = getTimeMillis()
                )
            )

            if (user != null) emit(State.Success(user.commonInfo()))

        } catch (e: Exception) {
            emit(State.Error(message = e.message))
        }

    }

    override fun login(login: String, password: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByLogin(login).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found"))
        else if (hashTool.checkHash(password, user.password)) {
            user = action(user)
            emit(State.Success(user.privateInfo()))
        }

    }

    override fun addAvatar(
        token: String,
        avatarId: Long
    ): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message="token expired"))
        else {user.avatar.add(avatarId); user = action(user); dao.editUser(user); emit(State.Success(user.privateInfo()))}
    }

    override fun deleteAvatar(token: String, avatarIndex: Int): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message="token expired"))
        else {user.avatar.removeAt(avatarIndex); user = action(user); dao.editUser(user); emit(State.Success(user.privateInfo()))}
    }


    override fun getUser(token: String, id: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        val requestedUser = dao.getById(id)
        if (user == null) emit(State.Expired(message="token expired"))
        else if (requestedUser == null) emit(State.Error(message="user not found"))
        else if (requestedUser.id == user.id) emit(State.Success(user.privateInfo())) else emit(State.Success(requestedUser.commonInfo()))

    }

    override fun getAll(): Flow<State<MutableList<User>>> = flow {
        emit(State.Processing())
        try {
            val userList = dao.getAll().map{it.smallInfo()}.toMutableList()
            emit(State.Success(data=userList))
        } catch (e: Exception) {emit(State.Error(message=e.message))}
    }

    override fun addRecipe(token: String, recipeId: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message="token expired"))
        else {user.userRecipes.add(recipeId); user = action(user); dao.editUser(user); emit(State.Success(user.privateInfo()))}
    }

    override fun deleteRecipe(token: String, recipeId: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message="token expired"))
        else {user.userRecipes.remove(recipeId); user = action(user); dao.editUser(user); emit(State.Success(user.privateInfo()))}
    }

    override fun addPost(token: String, postId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun addProducts(token: String, products: MutableList<Long>): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun deleteProducts(token: String, products: MutableList<Long>): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun starRecipe(token: String, recipeId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun starProduct(token: String, productId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun starPost(token: String, postId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun subscribe(token: String, userId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun addSubscriber(id: Long, userId: Long): Flow<State<User?>> {
        TODO("Not yet implemented")
    }

    override fun changePassword(token: String, password: String): Flow<State<User?>> {
        TODO()
    }

    override fun recoverAccess(code: String, login: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(login).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found"))
        else if (user.checkRecoveryCode(code)) emit(State.Success(user)) else emit(State.Error(message = "incorrect code"))
    }

    override fun recoveryCode(login: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(login).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found"))
        else {
            user.generateRecoveryCode()
            dao.editUser(user)
            emit(State.Success(user))
        }
    }

    override fun verify(code: String, email: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(email).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found"))
        else if (user.code == code) {
            dao.editUser(user); emit(State.Success(user))
        } else emit(State.Error(message = "incorrect code"))
    }

    override fun validatePassword(password: String): Boolean =
        when (validator.isValidLogin(password)) {
            is Validity.Valid -> true
            is Validity.Invalid -> false
        }

    override suspend fun nicknameAccessibility(nickname: String): Boolean =
        when (validator.isValidLogin(nickname)) {
            is Validity.Valid -> {
                dao.getByLogin(nickname).size == 0
            }

            is Validity.Invalid -> false
        }

    override suspend fun emailAccessibility(email: String): Boolean =
        when (validator.isValidLogin(email)) {
            is Validity.Valid -> {
                dao.getByLogin(email).size == 0
            }

            is Validity.Invalid -> false
        }

    override suspend fun action(user: User): User {
        user.lastSeen = getTimeMillis()
        dao.editUser(user)
        return user
    }

}