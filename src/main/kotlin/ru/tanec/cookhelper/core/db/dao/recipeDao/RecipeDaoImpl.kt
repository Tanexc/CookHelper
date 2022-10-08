package ru.tanec.cookhelper.core.db.dao.recipeDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.tanec.cookhelper.core.constants.DELIMITER
import ru.tanec.cookhelper.core.db.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.core.db.model.Recipes
import ru.tanec.cookhelper.enterprise.model.entity.Recipe

class RecipeDaoImpl : RecipeDao {

    private fun resultRowToRecipe(row: ResultRow) = Recipe(
        id = row[Recipes.id],

        owner = row[Recipes.owner],
        title = row[Recipes.title],
        cookSteps = row[Recipes.cookSteps].split(DELIMITER),
        time = row[Recipes.time],
        ingredients = row[Recipes.ingredients].split(" ").map { it.toLong() },

        proteins = row[Recipes.proteins],
        carbohydrates = row[Recipes.carbohydrates],
        fats = row[Recipes.fats],
        calories = row[Recipes.calories],

        image = row[Recipes.image],
        comments = row[Recipes.comments].split(" ").map { it.toLong() },
        reposts = row[Recipes.reposts].split(" ").map { it.toLong() },
        likes = row[Recipes.likes].split(" ").map { it.toLong() },

        )

    // part - index of list divided on pieces with length div
    private fun <T> List<T>.partOfDiv(part: Int, div: Int): List<T> {
        return if ((div * (part + 1) > this.size) and (div * part < this.size - 1)) {
            this.subList(div * part, this.size - 1)
        } else if (div * (part + 1) <= this.size) {
            this.subList(div * part, div * (part + 1) - 1)
        } else {
            this
        }

    }

    override suspend fun getAll(part: Int, div: Int): List<Recipe> = dbQuery {
        val data = Recipes
            .selectAll()
            .map(::resultRowToRecipe)

        data.partOfDiv(part, div)
    }

    override suspend fun getById(id: Long): Recipe? = dbQuery {
        Recipes
            .select{Recipes.id eq id}
            .map(::resultRowToRecipe)
            .singleOrNull()
    }

    override suspend fun getByUser(userId: Long, part: Int, div: Int): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun getByTitle(title: String, part: Int, div: Int): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun insertRecipe(recipe: Recipe): Recipe {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByIngredient(ingredient: Long, part: Int, div: Int): List<Recipe> {
        TODO("Not yet implemented")
    }

    override suspend fun getRecipeByIngredients(ingredient: List<Long>, part: Int, div: Int): List<Recipe> {
        TODO("Not yet implemented")
    }

}