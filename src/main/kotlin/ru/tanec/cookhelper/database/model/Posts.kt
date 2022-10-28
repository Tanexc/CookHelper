package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table


object Posts: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
    val text = text("text")
    val attachments = text("attachments")
    val label = text("label")
    val likes = text("likes")
    val comments = text("comments")
    val reposts = text("reposts")

    val timestamp = long("timestamp")
}