package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table

object Chats: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val avatar = text("avatar")
    val title = text("title")
    val members = text("members")
    val messages = text("messages")
    val attachments = text("attachments")
}