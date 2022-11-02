package ru.tanec.cookhelper.database.dao.ingredientDao

import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient

interface IngredientDao {

    suspend fun getAll(): List<Ingredient>

    suspend fun getAll(offset: Int, limit: Int): List<Ingredient>

    suspend fun getById(id: Long): Ingredient?

    suspend fun getByListId(listId: List<Long>, offset: Int, limit: Int): List<Ingredient>

}