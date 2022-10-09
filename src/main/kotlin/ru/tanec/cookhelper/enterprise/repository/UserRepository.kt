package ru.tanec.cookhelper.enterprise.repository

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.userDao.UserDao
import ru.tanec.cookhelper.core.utils.HashTool
import ru.tanec.cookhelper.core.utils.ValidatorImpl
import ru.tanec.cookhelper.enterprise.model.entity.User

interface UserRepository {
    val dao: UserDao
    val hashTool: HashTool
    val validator: ValidatorImpl
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

    fun addAvatar(
        token: String,
        avatarPath: String
    ): Flow<State<User?>>

    fun deleteAvatar(
        token: String,
        avatarIndex: Int
    ): Flow<State<User?>>


    fun getUser(
        token: String,
        id: Long
    ): Flow<State<User?>>

    fun getAll(): Flow<State<MutableList<User>>>

    fun addRecipe(
        token: String,
        recipeId: Long
    ): Flow<State<User?>>

    fun deleteRecipe(
        token: String,
        recipeId: Long
    ): Flow<State<User?>>

    fun addPost(
        token: String,
        postId: Long
    ): Flow<State<User?>>

    fun addProducts(
        token: String,
        products: MutableList<Long>
    ): Flow<State<User?>>

    fun deleteProducts(
        token: String,
        products: MutableList<Long>
    ): Flow<State<User?>>

    fun starRecipe(
        token: String,
        recipeId: Long
    ): Flow<State<User?>>

    fun starProduct(
        token: String,
        productId: Long
    ): Flow<State<User?>>

    fun starPost(
        token: String,
        postId: Long
    ): Flow<State<User?>>

    fun subscribe(
        token: String,
        userId: Long
    ): Flow<State<User?>>

    fun addSubscriber(
        id: Long,
        userId: Long
    ): Flow<State<User?>>

    fun changePassword(
        token: String,
        password: String
    ): Flow<State<User?>>

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

    suspend fun getByToken(token: String): Flow<State<User?>>
}