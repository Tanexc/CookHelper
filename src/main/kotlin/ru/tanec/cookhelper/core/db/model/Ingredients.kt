package ru.tanec.cookhelper.core.db.model

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Table.Dual.long
import ru.tanec.cookhelper.core.db.model.Users.autoIncrement

object Ingredients: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val title = text("title")
}