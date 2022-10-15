package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table


object Posts: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val text = text("text")
    val attachment = text("attachment")
    val images = text("images")

    val likes = text("likes")
    val comments = text("comments")
    val reposts = text("reposts")

    val timestamp = long("timestamp")
}