package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.ingredientDao.IngredientDao
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient

interface IngredientRepository {

    val dao: IngredientDao

    fun getAll(): Flow<State<List<Ingredient>?>>

    fun getAll(part: Int, div: Int): Flow<State<List<Ingredient>?>>

    fun getById(id: Long): Flow<State<Ingredient?>>

    fun getByListId(listId: List<Long>, offset: Int, limit: Int): Flow<State<List<Ingredient>>>

}
