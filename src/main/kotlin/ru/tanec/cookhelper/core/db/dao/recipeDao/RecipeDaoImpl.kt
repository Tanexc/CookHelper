package ru.tanec.cookhelper.core.db.dao.recipeDao

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
        ingredients = row[Recipes.ingredients].split(" ").mapNotNull { it.toLongOrNull() },
        category = row[Recipes.category],

        proteins = row[Recipes.proteins],
        carbohydrates = row[Recipes.carbohydrates],
        fats = row[Recipes.fats],
        calories = row[Recipes.calories],

        image = row[Recipes.image],
        comments = row[Recipes.comments].split(" ").mapNotNull { it.toLongOrNull() },
        reposts = row[Recipes.reposts].split(" ").mapNotNull { it.toLongOrNull() },
        likes = row[Recipes.likes].split(" ").mapNotNull { it.toLongOrNull() },

        )

    // part - index of list divided on pieces with length div
    private fun <T> List<T>.partOfDiv(part: Int, div: Int): List<T> {
        return if ((div * (part + 1) > this.size) and (div * part < this.size)) {
            this.subList(div * part, this.size)
        } else if (div * (part + 1) <= this.size) {
            this.subList(div * part, div * (part + 1))
        } else {
            listOf()
        }

    }

    override suspend fun getAll(part: Int, div: Int): List<Recipe> = dbQuery {
        val data = Recipes
            .selectAll()
            .map(::resultRowToRecipe)

        data.partOfDiv(part, div)
    }

    override suspend fun getAll(): List<Recipe> = dbQuery {
        Recipes
            .selectAll()
            .map(::resultRowToRecipe)
    }

    override suspend fun getById(id: Long): Recipe? = dbQuery {
        Recipes
            .select{Recipes.id eq id}
            .map(::resultRowToRecipe)
            .singleOrNull()
    }

    override suspend fun getByUser(userId: Long, part: Int, div: Int): List<Recipe> = dbQuery {
        Recipes
            .select{Recipes.owner eq userId}
            .map(::resultRowToRecipe)
            .partOfDiv(part, div)
    }

    override suspend fun getByTitle(title: String, part: Int, div: Int): List<Recipe> = dbQuery {
        Recipes
            .select{Recipes.title like title}
            .map(::resultRowToRecipe)
            .partOfDiv(part, div)
    }

    override suspend fun insertRecipe(recipe: Recipe): Recipe = dbQuery {
        Recipes
            .insert {
                it[title] = recipe.title
                it[cookSteps] = recipe.cookSteps.joinToString(DELIMITER)
                it[owner] = recipe.owner
                it[time] = recipe.time
                it[ingredients] = recipe.ingredients.joinToString(" ")
                it[proteins] = recipe.proteins
                it[calories] = recipe.calories
                it[fats] = recipe.fats
                it[carbohydrates] = recipe.carbohydrates
                it[image] = recipe.image
                it[comments] = recipe.comments.joinToString(" ")
                it[likes] = recipe.comments.joinToString(" ")
                it[reposts] = recipe.reposts.joinToString(" ")
            }
        recipe
    }

    override suspend fun getRecipeByIngredient(ingredient: Long, part: Int, div: Int): List<Recipe> = dbQuery {
        Recipes
            .select{(Recipes.ingredients.like("$ingredient ")) or (Recipes.ingredients.like(" $ingredient"))}
            .map(::resultRowToRecipe)
            .partOfDiv(part, div)
    }

    override suspend fun getRecipeByIngredients(ingredient: List<Long>, part: Int, div: Int): List<Recipe> = dbQuery {
        val data = getAll()
        data.mapNotNull { if (it.ingredients.intersect(ingredient).isNotEmpty()) it else null }
    }

    override suspend fun editRecipe(recipe: Recipe): Recipe = dbQuery {
        Recipes
            .update({Recipes.id eq recipe.id}) {
                it[title] = recipe.title
                it[cookSteps] = recipe.cookSteps.joinToString(DELIMITER)
                it[owner] = recipe.owner
                it[time] = recipe.time
                it[ingredients] = recipe.ingredients.joinToString(" ")
                it[proteins] = recipe.proteins
                it[calories] = recipe.calories
                it[fats] = recipe.fats
                it[carbohydrates] = recipe.carbohydrates
                it[image] = recipe.image
                it[comments] = recipe.comments.joinToString(" ")
                it[likes] = recipe.comments.joinToString(" ")
                it[reposts] = recipe.reposts.joinToString(" ")
            }
        recipe
    }

}