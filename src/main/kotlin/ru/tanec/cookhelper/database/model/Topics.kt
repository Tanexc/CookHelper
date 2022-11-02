package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table


object Topics: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val title = text("title")
    val text = text("text")
    val replies = text("replies")
    val timestamp = long("timestamp")
    val closed = bool("closed")
    val attachments = text("attachments")
    val tags = text("tags")
}