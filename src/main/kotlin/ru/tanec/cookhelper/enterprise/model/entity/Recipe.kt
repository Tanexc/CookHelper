package ru.tanec.cookhelper.enterprise.model.entity


data class Recipe(
    val id: Long = 0,

    val owner: Long,
    val title: String,
    val cookSteps: List<String>,
    val time: Long,
    val ingredients: List<Long>,

    val proteins: Double,
    val carbohydrates: Double,
    val fats: Double,
    val calories: Double,

    val image: String,
    val comments: List<Long>,
    val reposts: List<Long>,
    val likes: List<Long>,
)
