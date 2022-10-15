package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.SOME_ERROR
import ru.tanec.cookhelper.core.constants.SUCCESS
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.database.dao.recipeDao.RecipeDao
import ru.tanec.cookhelper.database.dao.recipeDao.RecipeDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository


class RecipeRepositoryImpl(
    override val dao: RecipeDao = RecipeDaoImpl()
) : RecipeRepository {
    override fun insert(recipe: Recipe): Flow<State<Recipe?>> = flow {
        emit(State.Processing())
        try {
            val data = dao.insertRecipe(recipe)
            emit(State.Success(message= SUCCESS, status=RecipeStatus.SUCCESS, data=data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: SOME_ERROR, status = RecipeStatus.EXCEPTION))
        }

    }

    override fun getById(id: Long): Flow<State<Recipe?>> = flow {
        emit(State.Processing())
        when (val recipe = dao.getById(id)) {
            is Recipe -> emit(State.Success(recipe, status = RecipeStatus.SUCCESS))
            else -> emit(State.Error(status = RecipeStatus.RECIPE_NOT_FOUND))
        }

    }

    override fun getByTitle(title: String, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByTitle(title, part, div), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }


    override fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getAll(part, div), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }

    }

    override fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByUser(userId, part, div), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

    override fun getRecipeByIngredient(ingredient: Long, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getRecipeByIngredient(ingredient, part, div), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

    override fun getRecipeByIngredients(ingredient: List<Long>, part: Int, div: Int): Flow<State<List<Recipe>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getRecipeByIngredients(ingredient, part, div), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

    override fun editRecipe(recipe: Recipe): Flow<State<Recipe>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.editRecipe(recipe), status = RecipeStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = RecipeStatus.EXCEPTION))
        }
    }

}