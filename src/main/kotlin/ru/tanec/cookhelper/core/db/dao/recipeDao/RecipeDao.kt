package ru.tanec.cookhelper.core.db.dao.recipeDao

import ru.tanec.cookhelper.enterprise.model.entity.Recipe

interface RecipeDao {
    suspend fun getAll(part: Int, div: Int): List<Recipe>

    suspend fun getById(id: Long): Recipe?

    suspend fun getByUser(userId: Long, part: Int, div: Int): List<Recipe>

    suspend fun getByTitle(title: String, part: Int, div: Int): List<Recipe>

    suspend fun insertRecipe(recipe: Recipe): Recipe

    suspend fun getRecipeByIngredient(ingredient: Long, part: Int, div: Int): List<Recipe>

    suspend fun getRecipeByIngredients(ingredient: List<Long>, part: Int, div: Int): List<Recipe>
}