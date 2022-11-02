package ru.tanec.cookhelper.database.dao.recipeDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.DELIMITER
import ru.tanec.cookhelper.core.utils.filterMap
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.core.utils.intersectionMoreThan
import ru.tanec.cookhelper.core.utils.partOfDiv
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Recipes
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe

class RecipeDaoImpl : RecipeDao {
    private fun resultRowToRecipe(row: ResultRow) = Recipe(
        id = row[Recipes.id],

        authorId = row[Recipes.authorId],
        title = row[Recipes.title],
        cookSteps = row[Recipes.cookSteps].split(DELIMITER),
        time = row[Recipes.time],
        ingredients = row[Recipes.ingredients].split(" ").mapNotNull { it.toLongOrNull() },
        category = row[Recipes.category],

        proteins = row[Recipes.proteins],
        carbohydrates = row[Recipes.carbohydrates],
        fats = row[Recipes.fats],
        calories = row[Recipes.calories],

        image = toFileData(row[Recipes.image]) ?: FileData(-1, "notFound", "notFound", "notFound"),
        comments = row[Recipes.comments].split(" ").mapNotNull { it.toLongOrNull() },
        reposts = row[Recipes.reposts].split(" ").mapNotNull { it.toLongOrNull() },
        likes = row[Recipes.likes].split(" ").mapNotNull { it.toLongOrNull() },
        timestamp = row[Recipes.timestamp]
    )

    private fun ResultRow.toRecipe() = Recipe(
        id = this[Recipes.id],

        authorId = this[Recipes.authorId],
        title = this[Recipes.title],
        cookSteps = this[Recipes.cookSteps].split(DELIMITER),
        time = this[Recipes.time],
        ingredients = this[Recipes.ingredients].split(" ").mapNotNull { it.toLongOrNull() },
        category = this[Recipes.category],

        proteins = this[Recipes.proteins],
        carbohydrates = this[Recipes.carbohydrates],
        fats = this[Recipes.fats],
        calories = this[Recipes.calories],

        image = toFileData(this[Recipes.image]) ?: FileData(-1, "notFound", "notFound", "notFound"),
        comments = this[Recipes.comments].split(" ").mapNotNull { it.toLongOrNull() },
        reposts = this[Recipes.reposts].split(" ").mapNotNull { it.toLongOrNull() },
        likes = this[Recipes.likes].split(" ").mapNotNull { it.toLongOrNull() },
        timestamp = this[Recipes.timestamp]
    )

    //TODO(change offset and limit)

    override suspend fun getAll(offset: Int, limit: Int): List<Recipe> = dbQuery {
        val data = Recipes
            .selectAll()
            .map(::resultRowToRecipe)

        data.getPage(offset, limit)
    }

    override suspend fun getAll(): List<Recipe> = dbQuery {
        Recipes
            .selectAll()
            .map(::resultRowToRecipe)
    }

    override suspend fun getById(id: Long): Recipe? = dbQuery {
        Recipes
            .select { Recipes.id eq id }
            .map(::resultRowToRecipe)
            .singleOrNull()
    }

    override suspend fun getByUser(userId: Long, offset: Int, limit: Int): List<Recipe> = dbQuery {
        Recipes
            .select { Recipes.authorId eq userId }
            .map(::resultRowToRecipe)
            .partOfDiv(offset, limit)
    }

    override suspend fun getByTitle(title: String, offset: Int, limit: Int): List<Recipe> = dbQuery {
        Recipes
            .select { Recipes.title like title }
            .map(::resultRowToRecipe)
            .partOfDiv(offset, limit)
    }

    override suspend fun insertRecipe(recipe: Recipe): Recipe = dbQuery {
        Recipes
            .insert {
                it[title] = recipe.title
                it[cookSteps] = recipe.cookSteps.joinToString(DELIMITER)
                it[authorId] = recipe.authorId ?: 0
                it[time] = recipe.time
                it[ingredients] = recipe.ingredients.joinToString(" ")
                it[proteins] = recipe.proteins
                it[calories] = recipe.calories
                it[fats] = recipe.fats
                it[carbohydrates] = recipe.carbohydrates
                it[image] = recipe.image.name
                it[comments] = recipe.comments.joinToString(" ")
                it[likes] = recipe.comments.joinToString(" ")
                it[reposts] = recipe.reposts.joinToString(" ")
                it[category] = recipe.category
                it[timestamp] = recipe.timestamp
            }
        Recipes
            .select { Recipes.timestamp eq recipe.timestamp }
            .map(::resultRowToRecipe)
            .single()
    }

    override suspend fun getRecipeByIngredient(ingredient: Long, offset: Int, limit: Int): List<Recipe> = dbQuery {
        Recipes
            .select { (Recipes.ingredients.like("$ingredient ")) or (Recipes.ingredients.like(" $ingredient")) }
            .map(::resultRowToRecipe)
            .getPage(limit, offset)
    }

    override suspend fun getRecipeByIngredients(ingredient: List<Long>, offset: Int, limit: Int): List<Recipe> =
        dbQuery {
            Recipes
                .selectAll()
                .sortedBy { it.toRecipe().ingredients.size }
                .filterMap(predicate =
                { recipe ->
                    recipe.ingredients.intersectionMoreThan(ingredient, 0.33)
                })
                { row ->
                    row.toRecipe()
                }
                .sortedBy { it.ingredients.intersect(ingredient.toSet()).size }
                .getPage(limit, offset)
        }

    override suspend fun editRecipe(recipe: Recipe): Recipe = dbQuery {
        Recipes
            .update({ Recipes.id eq recipe.id }) {
                it[title] = recipe.title
                it[cookSteps] = recipe.cookSteps.joinToString(DELIMITER)
                it[time] = recipe.time
                it[ingredients] = recipe.ingredients.joinToString(" ")
                it[proteins] = recipe.proteins
                it[calories] = recipe.calories
                it[fats] = recipe.fats
                it[carbohydrates] = recipe.carbohydrates
                it[image] = recipe.image.name
                it[comments] = recipe.comments.joinToString(" ")
                it[likes] = recipe.comments.joinToString(" ")
                it[reposts] = recipe.reposts.joinToString(" ")
                it[category] = recipe.category
            }
        recipe
    }

}