package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.SOME_ERROR
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.database.dao.recipeDao.RecipeDao
import ru.tanec.cookhelper.database.dao.recipeDao.RecipeDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository


class RecipeRepositoryImpl(
    override val dao: RecipeDao = RecipeDaoImpl()
) : RecipeRepository {
    override fun insert(recipe: Recipe): Flow<State<Recipe?>> = flow {
        emit(State.Processing())
        try {
            val data = dao.insertRecipe(recipe)
            emit(State.Success(status = SUCCESS, data = data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: SOME_ERROR, status = EXCEPTION))
        }

    }

    override fun getById(id: Long): Flow<State<Recipe?>> = flow {
        emit(State.Processing())
        when (val recipe = dao.getById(id)) {
            is Recipe -> emit(State.Success(recipe, status = SUCCESS))
            else -> emit(State.Error(status = RECIPE_NOT_FOUND))
        }

    }

    override fun getByTitle(title: String, offset: Int, limit: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByTitle(title, offset, limit), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }


    override fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getAll(part, div), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }

    }

    override fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByUser(userId, part, div), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun getRecipeByIngredient(ingredient: Long, offset: Int, limit: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getRecipeByIngredient(ingredient, offset, limit), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun getRecipeByIngredients(ingredient: List<Long>, offset: Int, limit: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getRecipeByIngredients(ingredient, offset, limit), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun editRecipe(recipe: Recipe): Flow<State<Recipe>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.editRecipe(recipe), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

}