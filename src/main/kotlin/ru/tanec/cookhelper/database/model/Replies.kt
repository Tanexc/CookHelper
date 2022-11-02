package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table

object Replies: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val text = text("text")
    val attachments = text("attachments")
    val replyToId = long("replyToId")
    val timestamp = long("timestamp")
    val ratingPositive = text("ratingPositive")
    val ratingNegative = text("ratingNegative")
    val replies = text("replies")
}