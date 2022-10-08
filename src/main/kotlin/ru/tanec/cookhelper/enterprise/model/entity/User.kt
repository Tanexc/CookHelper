package ru.tanec.cookhelper.enterprise.model.entity

import kotlinx.serialization.Serializable
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

    var avatar: MutableList<Long> = mutableListOf(),
    val fridge: MutableList<Long> = mutableListOf(),
    val topics: MutableList<Long> = mutableListOf(),
    val starredRecipes: MutableList<Long> = mutableListOf(),
    val bannedRecipes: MutableList<Long> = mutableListOf(),
    val starredIngredients: MutableList<Long> = mutableListOf(),
    val bannedIngredients: MutableList<Long> = mutableListOf(),
    val chats: MutableList<Long> = mutableListOf(),
    val starredPosts: MutableList<Long> = mutableListOf(),

    val subscribers: MutableList<Long> = mutableListOf(),
    val subscribes: MutableList<Long> = mutableListOf(),

    val userRecipes: MutableList<Long> = mutableListOf(),
    val userPosts: MutableList<Long> = mutableListOf(),
    val registrationTimestamp: Long? = null


) {

    fun getPsw() = password
    fun generateRecoveryCode() {
        recoveryCode = mutableListOf(
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
                userPosts = this.userPosts
            )
        } else User(deleted = this.deleted)
    }

    fun smallInfo(): User = User(
        id = this.id,
        name = this.name,
        surname = this.surname,
        avatar = mutableListOf(this.avatar[this.avatar.size - 1]),
        lastSeen = this.lastSeen,
    )

}
