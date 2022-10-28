package ru.tanec.cookhelper.enterprise.model.entity.user

import kotlinx.serialization.Serializable
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import kotlin.random.Random

@Serializable
data class User(
    val id: Long = 0,

    var name: String = "",
    var surname: String = "",
    var nickname: String = "",
    var email: String = "",
    var password: ByteArray = byteArrayOf(),

    var status: String = "",
    var lastSeen: Long = 0,

    var deleted: Boolean = false,
    var verified: Boolean = true,
    var code: String = "",
    var recoveryCode: String = "",
    var token: String = "",

    var avatar: List<FileData> = listOf(),
    val images: List<FileData> = emptyList(),


    val fridge: List<Long> = listOf(),
    val topics: List<Long> = listOf(),
    val starredRecipes: List<Long> = listOf(),
    val bannedRecipes: List<Long> = listOf(),
    val starredIngredients: List<Long> = listOf(),
    val bannedIngredients: List<Long> = listOf(),
    val chats: List<Long> = listOf(),
    val starredPosts: List<Long> = listOf(),

    val subscribers: List<Long> = listOf(),
    val subscribes: List<Long> = listOf(),

    val userRecipes: List<Long> = listOf(),
    val userPosts: List<Long> = listOf(),
    val registrationTimestamp: Long? = null


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
        subscribes = this.subscribes
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
