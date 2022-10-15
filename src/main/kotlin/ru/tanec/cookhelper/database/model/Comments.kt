package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table

object Comments: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val text = text("text")
    val timestamp = long("timestamp")
}