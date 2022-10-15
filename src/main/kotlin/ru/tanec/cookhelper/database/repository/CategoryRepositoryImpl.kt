package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.database.dao.categoryDao.CategoryDao
import ru.tanec.cookhelper.database.dao.categoryDao.CategoryDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Category
import ru.tanec.cookhelper.enterprise.repository.CategoryRepository

class CategoryRepositoryImpl(
    override val dao: CategoryDao = CategoryDaoImpl()
) : CategoryRepository {
    override fun getAll(): Flow<State<List<Category>?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(status = RecipeStatus.SUCCESS, data = dao.getAll()))
        } catch (_: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

    override fun getAll(part: Int, div: Int): Flow<State<List<Category>?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(status = RecipeStatus.SUCCESS, data = dao.getAll(part, div)))
        } catch (_: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

    override fun getById(id: Long): Flow<State<Category?>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(status = RecipeStatus.SUCCESS, data = dao.getById(id)))
        } catch (_: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }
}