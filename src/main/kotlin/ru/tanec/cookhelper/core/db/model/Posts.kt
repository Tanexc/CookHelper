package ru.tanec.cookhelper.core.db.model

import org.jetbrains.exposed.sql.Table
import ru.tanec.cookhelper.core.db.model.Users.entityId


object Posts: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val owner = long("owner")
    val text = text("text")
    val attachment = text("attachment")
    val images = text("images")

    val likes = text("likes")
    val comments = text("comments")
    val reposts = text("reposts")

    val timestamp = long("timestamp")
}