package ru.tanec.cookhelper.database.dao.ingredientDao

import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient

interface IngredientDao {

    suspend fun getAll(): List<Ingredient>

    suspend fun getAll(part: Int, div: Int): List<Ingredient>

    suspend fun getById(id: Long): Ingredient?

}