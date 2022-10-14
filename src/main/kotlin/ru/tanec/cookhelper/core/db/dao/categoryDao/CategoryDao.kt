package ru.tanec.cookhelper.core.db.dao.categoryDao

import ru.tanec.cookhelper.enterprise.model.entity.Category

interface CategoryDao {
    suspend fun getAll(): List<Category>

    suspend fun getAll(part: Int, div: Int): List<Category>

    suspend fun getById(id: Long): Category?
}