package ru.tanec.cookhelper.database.repository

import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.*
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
    override fun register(
        user: User
    ): Flow<State<User?>> = flow {
        emit(State.Processing())

        if (!nicknameAccessibility(user.nickname)) throw Exception("nickname rejected")
        if (!emailAccessibility(user.email)) throw Exception("login rejected")

        val data = dao.addNew(
            user.copy(registrationTimestamp = getTimeMillis())
        )


        if (data != null) emit(State.Success(data.commonInfo(), status = UserStatus.SUCCESS))
        else emit(State.Error(status=UserStatus.EXCEPTION))


    }

    override fun login(login: String, password: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByLogin(login).singleOrNull()

        emit(State.Processing())

        if (user == null) emit(State.Error(status = UserStatus.EXCEPTION))
        else if (hashTool.verifyHash(password, user.password)) {
            if (user.token == "") user.token = generateToken(
                user.name,
                user.surname,
                user.registrationTimestamp ?: 0L,
                user.password.toString()
            )
            user = action(user)
            emit(State.Success(user.privateInfo(), status = UserStatus.SUCCESS))
        } else emit(State.Error(status = UserStatus.USER_NOT_FOUND))

    }

    override fun addAvatar(
        token: String,
        avatarPath: String
    ): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = UserStatus.EXCEPTION))
        else {
            user.avatar.add(avatarPath); user = action(user); dao.editUser(user); emit(
                State.Success(
                    user.privateInfo(),
                    status = UserStatus.SUCCESS
                )
            )
        }
    }

    override fun deleteAvatar(token: String, avatarIndex: Int): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = UserStatus.TOKEN_EXPIRED))
        else {
            user.avatar.removeAt(avatarIndex); user =
                action(user); dao.editUser(user); emit(State.Success(user.privateInfo(), status = UserStatus.SUCCESS))
        }
    }


    override fun getUser(token: String, id: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByToken(token)
        val requestedUser = dao.getById(id)
        if (user == null) emit(State.Expired(message = "token expired", status = UserStatus.TOKEN_EXPIRED))
        else if (requestedUser == null) emit(
            State.Error(
                message = "user not found",
                status = UserStatus.USER_NOT_FOUND
            )
        )
        else if (requestedUser.id == user.id) emit(
            State.Success(
                user.privateInfo(),
                status = UserStatus.SUCCESS
            )
        ) else emit(State.Success(requestedUser.commonInfo(), status = UserStatus.SUCCESS))

    }

    override fun getAll(): Flow<State<MutableList<User>>> = flow {
        emit(State.Processing())
        try {
            val userList = dao.getAll().map { it.smallInfo() }.toMutableList()
            emit(State.Success(data = userList, status = UserStatus.SUCCESS))
        } catch (e: Exception) {
            emit(State.Error(status = UserStatus.EXCEPTION))
        }
    }

    override fun addRecipe(token: String, recipeId: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = UserStatus.TOKEN_EXPIRED))
        else {
            user.userRecipes.add(recipeId); user =
                action(user); dao.editUser(user); emit(State.Success(user.privateInfo(), status = UserStatus.SUCCESS))
        }
    }

    override fun deleteRecipe(token: String, recipeId: Long): Flow<State<User?>> = flow {
        emit(State.Processing())
        var user = dao.getByToken(token)
        if (user == null) emit(State.Expired(message = "token expired", status = UserStatus.TOKEN_EXPIRED))
        else {
            user.userRecipes.remove(recipeId); user =
                action(user); dao.editUser(user); emit(State.Success(user.privateInfo(), status = UserStatus.SUCCESS))
        }
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
        if (user == null) emit(State.Error(message = "user not found", status = UserStatus.USER_NOT_FOUND))
        else if (user.checkRecoveryCode(code)) emit(
            State.Success(
                user,
                status = UserStatus.SUCCESS
            )
        ) else emit(State.Error(message = "incorrect code", status = UserStatus.WRONG_CREDENTAILS))
    }

    override fun recoveryCode(login: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(login).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found", status = UserStatus.USER_NOT_FOUND))
        else {
            user.generateRecoveryCode()
            dao.editUser(user)
            emit(State.Success(user, status = UserStatus.SUCCESS))
        }
    }

    override fun verify(code: String, email: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByLogin(email).singleOrNull()
        if (user == null) emit(State.Error(message = "user not found", status = UserStatus.USER_NOT_FOUND))
        else if (user.code == code) {
            dao.editUser(user); emit(State.Success(user, status = UserStatus.SUCCESS))
        } else emit(State.Error(message = "incorrect code", status = UserStatus.WRONG_CREDENTAILS))
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

    override suspend fun getByToken(token: String): Flow<State<User?>> = flow {
        emit(State.Processing())
        val user = dao.getByToken(token)
        if (user != null) emit(State.Success(user, status = UserStatus.SUCCESS))
        else emit(State.Error(status=UserStatus.USER_NOT_FOUND))
    }

}