package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table

object Messages: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val text = text("text")
    val attachments = text("attachments")
    val replyToId = long("replyToId")
    val timestamp = long("timestamp")
    val views = text("views")
}