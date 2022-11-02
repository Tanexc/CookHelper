package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.ingredientDao.IngredientDao
import ru.tanec.cookhelper.database.dao.ingredientDao.IngredientDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository

class IngredientRepositoryImpl(
    override val dao: IngredientDao = IngredientDaoImpl()
) : IngredientRepository {
    override fun getAll(): Flow<State<List<Ingredient>?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(data = dao.getAll()))
        } catch (_: Exception) {
            emit(State.Error())
        }
    }

    override fun getAll(part: Int, div: Int): Flow<State<List<Ingredient>?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(data = dao.getAll(part, div)))
        } catch (_: Exception) {
            emit(State.Error())
        }
    }

    override fun getById(id: Long): Flow<State<Ingredient?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success( data = dao.getById(id)))
        } catch (_: Exception) {
            emit(State.Error())
        }
    }

    override fun getByListId(listId: List<Long>, offset: Int, limit: Int): Flow<State<List<Ingredient>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success( data = dao.getByListId(listId, offset, limit)))
        } catch (_: Exception) {
            emit(State.Error())
        }
    }
}