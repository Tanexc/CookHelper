package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table


object Topics: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val title = text("title")
    val problem = text("problem")
    val answers = text("answers")
    val timestamp = long("timestamp")
    val closed = bool("closed")
    val attachments = text("attachments")
}