package ru.tanec.cookhelper.enterprise.model.entity.user

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import kotlin.random.Random

@Serializable
data class User @OptIn(ExperimentalSerializationApi::class) constructor(
    @EncodeDefault val id: Long = 0,

    @EncodeDefault val name: String = "",
    @EncodeDefault val surname: String = "",
    @EncodeDefault val nickname: String = "",
    @EncodeDefault val email: String = "",
    @EncodeDefault val password: ByteArray = byteArrayOf(),

    @EncodeDefault val status: String = "",
    @EncodeDefault val lastSeen: Long = 0,

    @EncodeDefault val deleted: Boolean = false,
    @EncodeDefault val verified: Boolean = true,
    @EncodeDefault val code: String = "",
    @EncodeDefault var recoveryCode: String = "",
    @EncodeDefault val token: String = "",

    @EncodeDefault val avatar: List<FileData> = listOf(),
    @EncodeDefault val images: List<FileData> = emptyList(),


    @EncodeDefault val fridge: List<Long> = listOf(),
    @EncodeDefault val topics: List<Long> = listOf(),
    @EncodeDefault val starredRecipes: List<Long> = listOf(),
    @EncodeDefault val bannedRecipes: List<Long> = listOf(),
    @EncodeDefault val starredIngredients: List<Long> = listOf(),
    @EncodeDefault val bannedIngredients: List<Long> = listOf(),
    @EncodeDefault val chats: List<Long> = listOf(),
    @EncodeDefault val starredPosts: List<Long> = listOf(),

    @EncodeDefault val subscribers: List<Long> = listOf(),
    @EncodeDefault val subscribes: List<Long> = listOf(),

    @EncodeDefault val userRecipes: List<Long> = listOf(),
    @EncodeDefault val userPosts: List<Long> = listOf(),
    @EncodeDefault val registrationTimestamp: Long? = null


) {

    fun getPsw() = password
    fun generateRecoveryCode() {
        recoveryCode = listOf(
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10),
            Random.nextInt(0, 10)
        ).joinToString()
    }

    fun checkRecoveryCode(input: String) = recoveryCode == input

    fun privateInfo() = User(
        id = this.id,
        name = this.name,
        surname = this.surname,
        nickname = this.nickname,
        email = this.email,
        avatar = this.avatar,
        lastSeen = this.lastSeen,
        status = this.status,
        deleted = this.deleted,
        verified = this.verified,
        token = this.token,
        images = this.images,

        fridge = this.fridge,
        topics = this.topics,
        starredRecipes = this.starredRecipes,
        bannedRecipes = this.bannedRecipes,
        starredIngredients = this.starredIngredients,
        bannedIngredients = this.bannedIngredients,
        chats = this.chats,
        userRecipes = this.userRecipes,
        userPosts = this.userPosts,
        starredPosts = this.starredPosts,
        subscribers = this.subscribers,
        subscribes = this.subscribes,
    )

    fun commonInfo(): User {
        return if (!this.deleted) {
            User(
                id = this.id,
                name = this.name,
                surname = this.surname,
                nickname = this.nickname,
                email = this.email,
                avatar = this.avatar,
                lastSeen = this.lastSeen,
                status = this.status,
                deleted = this.deleted,
                userRecipes = this.userRecipes,
                userPosts = this.userPosts,
                images = this.images,
            )
        } else User(deleted = this.deleted)
    }

    fun smallInfo(): User = User(
        id = this.id,
        name = this.name,
        surname = this.surname,
        avatar = this.avatar.lastOrNull()?.let{listOf(it)}?: emptyList(),
        lastSeen = this.lastSeen,
    )

}
