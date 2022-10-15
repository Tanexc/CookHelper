package ru.tanec.cookhelper.database.dao.categoryDao

import ru.tanec.cookhelper.enterprise.model.entity.recipe.Category

interface CategoryDao {
    suspend fun getAll(): List<Category>

    suspend fun getAll(part: Int, div: Int): List<Category>

    suspend fun getById(id: Long): Category?
}