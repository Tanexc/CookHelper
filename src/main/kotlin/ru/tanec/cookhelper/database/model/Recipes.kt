package ru.tanec.cookhelper.database.model

import org.jetbrains.exposed.sql.Table


object Recipes: Table() {
    val id = long("id").autoIncrement()

    override val primaryKey = PrimaryKey(id)

    val authorId = long("authorId")
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