package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.categoryDao.CategoryDao
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Category

interface CategoryRepository {
    val dao: CategoryDao

    fun getAll(): Flow<State<List<Category>?>>

    fun getAll(part: Int, div: Int): Flow<State<List<Category>?>>

    fun getById(id: Long): Flow<State<Category?>>

}
