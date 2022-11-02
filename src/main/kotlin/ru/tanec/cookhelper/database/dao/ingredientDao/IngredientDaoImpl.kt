package ru.tanec.cookhelper.database.dao.ingredientDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.tanec.cookhelper.core.utils.getPage
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Ingredients
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Ingredient

class IngredientDaoImpl: IngredientDao {

    private fun resultRowToIngredient(row: ResultRow): Ingredient = Ingredient(
        id = row[Ingredients.id],
        title = row[Ingredients.title]
    )

    private fun ResultRow.toIngredient() = Ingredient(
        id=this[Ingredients.id],
        title=this[Ingredients.title]
    )


    override suspend fun getAll(): List<Ingredient> = dbQuery {

        Ingredients
            .selectAll()
            .map(::resultRowToIngredient)
    }

    override suspend fun getAll(offset: Int, limit: Int): List<Ingredient> = dbQuery {
        Ingredients
            .selectAll()
            .map(::resultRowToIngredient)
            .getPage(offset, limit)
    }

    override suspend fun getById(id: Long): Ingredient? = dbQuery {
        Ingredients
            .select { Ingredients.id eq id}
            .map(::resultRowToIngredient)
            .singleOrNull()
    }

    override suspend fun getByListId(listId: List<Long>, offset: Int, limit: Int): List<Ingredient> = dbQuery {
        Ingredients
            .select { Ingredients.id inList listId }
            .sortedBy { it.toIngredient().title }
            .getPage(limit, offset)
            .map(::resultRowToIngredient)
    }
}