package ru.tanec.cookhelper.core.db.model

import org.jetbrains.exposed.sql.Table

object Categories: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val title = text("title")
}