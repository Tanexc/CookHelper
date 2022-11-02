package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table

object FileDatas: Table() {
    val id = long("id")

    override val primaryKey = PrimaryKey(id)

    val name = text("name")
    val link = text("link")
    val type = text("type")
}