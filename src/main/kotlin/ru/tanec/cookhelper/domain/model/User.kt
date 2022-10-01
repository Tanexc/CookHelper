package ru.tanec.cookhelper.domain.model

import kotlinx.serialization.Serializable
import kotlin.random.Random

@Serializable
data class User(
    val id: Long,

    var name: String,
    var surname: String,
    var nickname: String,
    var email: String,
    var password: String,

    var avatar: String = "",
    var status: String = "",
    var lastSeen: Long,

    var deleted: Boolean = false,
    var verified: Boolean = false,
    var code: String = "",
    var recoveryCode: String = "",
    var token: String = "",

    val fridge: MutableList<Int> = mutableListOf(),
    val topics: MutableList<Int> = mutableListOf(),
    val starredRecipes: MutableList<Int> = mutableListOf(),
    val bannedRecipes: MutableList<Int> = mutableListOf(),
    val starredIngredients: MutableList<Int> = mutableListOf(),
    val bannedIngredients: MutableList<Int> = mutableListOf(),
    val chats: MutableList<Int> = mutableListOf(),

    val userRecipes: MutableList<Int> = mutableListOf(),
    val userPosts: MutableList<Int> = mutableListOf()


) {
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
}
