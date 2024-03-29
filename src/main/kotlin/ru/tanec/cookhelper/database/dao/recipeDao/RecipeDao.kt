package ru.tanec.cookhelper.database.dao.recipeDao

import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe

interface RecipeDao {

    suspend fun getAll(): List<Recipe>
    suspend fun getAll(offset: Int, limit: Int): List<Recipe>

    suspend fun getById(id: Long): Recipe?

    suspend fun getByUser(userId: Long, offset: Int, limit: Int): List<Recipe>

    suspend fun getByTitle(title: String, offset: Int, limit: Int): List<Recipe>

    suspend fun insertRecipe(recipe: Recipe): Recipe

    suspend fun getRecipeByIngredient(ingredient: Long, offset: Int, limit: Int): List<Recipe>

    suspend fun getRecipeByIngredients(ingredient: List<Long>, offset: Int, limit: Int): List<Recipe>

    suspend fun editRecipe(recipe: Recipe): Recipe

}