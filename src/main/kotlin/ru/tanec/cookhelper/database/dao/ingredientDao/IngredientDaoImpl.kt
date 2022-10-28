package ru.tanec.cookhelper.database.dao.ingredientDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Ingredients
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient

class IngredientDaoImpl: IngredientDao {

    private fun resultRowToIngredient(row: ResultRow): Ingredient = Ingredient(
        id = row[Ingredients.id],
        title = row[Ingredients.title]
    )

    private fun <T> List<T>.partOfDiv(part: Int, div: Int): List<T> {
        return if ((div * (part + 1) > this.size) and (div * part < this.size)) {
            this.subList(div * part, this.size)
        } else if (div * (part + 1) <= this.size) {
            this.subList(div * part, div * (part + 1))
        } else {
            listOf()
        }

    }

    override suspend fun getAll(): List<Ingredient> = dbQuery {

        Ingredients
            .selectAll()
            .map(::resultRowToIngredient)
    }

    override suspend fun getAll(part: Int, div: Int): List<Ingredient> = dbQuery {
        Ingredients
            .selectAll()
            .map(::resultRowToIngredient)
            .partOfDiv(part, div)
    }

    override suspend fun getById(id: Long): Ingredient? = dbQuery {
        Ingredients
            .select { Ingredients.id eq id}
            .map(::resultRowToIngredient)
            .singleOrNull()
    }
}