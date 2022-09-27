package ru.tanec.cookhelper.domain.model

data class User(
    val id: Long,

    val name: String,
    val surname: String,
    val nickname: String,
    val email: String,
    val password: String,

    val avatar: String,
    val lastSeen: Long,
    val status: String,

    val deleted: Boolean,
    val verified: Boolean,
    val code: String,
    val recoveryCode: String,
    val token: String,

    val fridge: MutableList<Int>,
    val topics: MutableList<Int>,
    val starredRecipes: MutableList<Int>,
    val bannedRecipes: MutableList<Int>,
    val starredIngredients: MutableList<Int>,
    val bannedIngredients: MutableList<Int>,
    val chats: MutableList<Int>,

    val userRecipes: MutableList<Int>,
    val userPosts: MutableList<Int>


)
