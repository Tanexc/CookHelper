package ru.tanec.cookhelper.enterprise.repository

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.ingredientDao.IngredientDao
import ru.tanec.cookhelper.enterprise.model.entity.Ingredient

interface IngredientRepository {

    val dao: IngredientDao

    fun getAll(): Flow<State<List<Ingredient>?>>

    fun getAll(part: Int, div: Int): Flow<State<List<Ingredient>?>>

    fun getById(id: Long): Flow<State<Ingredient?>>

}
