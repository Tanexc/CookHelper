package ru.tanec.cookhelper.enterprise.repository.api

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.database.dao.recipeDao.RecipeDao
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe

interface RecipeRepository {
    val dao: RecipeDao

    fun insert(recipe: Recipe): Flow<State<Recipe?>>

    fun getById(id: Long): Flow<State<Recipe?>>

    fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Recipe>>>

    fun getByTitle(title: String, offset: Int, limit: Int): Flow<State<List<Recipe>>> // TODO(offset, limit)

    fun getRecipeByIngredient(ingredient: Long, offset: Int, limit: Int): Flow<State<List<Recipe>>>

    fun getRecipeByIngredients(ingredient: List<Long>, offset: Int, limit: Int): Flow<State<List<Recipe>>>

    fun editRecipe(recipe: Recipe): Flow<State<Recipe>>
}