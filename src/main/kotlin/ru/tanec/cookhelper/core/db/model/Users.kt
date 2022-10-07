package ru.tanec.cookhelper.core.db.model

import org.jetbrains.exposed.sql.Table

object Users: Table() {
    val id = long("id").autoIncrement()

    val name = text("name")
    val surname = text("surname")
    val nickname = text("nickname").uniqueIndex()
    val email = text("email").uniqueIndex()
    val password = binary("password", length=256)

    val avatar = text("avatar")
    val lastSeen = long("lastSeen")
    val status = text("status")

    val deleted = bool("deleted")
    val verified = bool("verified")
    val code = varchar("code", 6)
    val recoveryCode = varchar("recoveryCode", 6)
    val token = varchar("token", 512)

    val fridge = text("fridge")
    val topics = text("topics")
    val starredRecipes = text("starredRecipes")
    val bannedRecipes = text("bannedRecipes")
    val starredIngredients = text("starredIngredients")
    val bannedIngredients = text("bannedIngredients")
    val chats = text("chats")

    val subscribers = text("subscribers")
    val subscribes = text("subscribes")
    val userRecipes = text("userRecipes")
    val userPosts = text("userPosts")
    val registrationTimestamp = long("registrationTimestamp")
}