package ru.tanec.cookhelper.core.db.dao.ingredientDao

import ru.tanec.cookhelper.enterprise.model.entity.Ingredient

interface IngredientDao {

    suspend fun getAll(): List<Ingredient>

    suspend fun getAll(part: Int, div: Int): List<Ingredient>

    suspend fun getById(id: Long): Ingredient?

}