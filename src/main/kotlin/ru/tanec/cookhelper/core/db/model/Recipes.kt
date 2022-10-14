package ru.tanec.cookhelper.core.db.model

import org.jetbrains.exposed.sql.Table
import ru.tanec.cookhelper.core.db.model.Users.entityId


object Recipes: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val owner = long("userId")
    val title = text("title")
    val cookSteps = text("cookSteps")
    val time = long("time")
    val ingredients = text("ingredients")
    val category = long("category")

    val proteins = double("proteins")
    val carbohydrates = double("carbohydrates")
    val fats = double("fats")
    val calories = double("calories")

    val image = text("image")
    val comments = text("comments")
    val reposts = text("reposts")
    val likes = text("likes")
    val timestamp = long("timestamp")

}