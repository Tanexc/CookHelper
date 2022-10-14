package ru.tanec.cookhelper.core.db.dao.categoryDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import ru.tanec.cookhelper.core.db.factory.DatabaseFactory
import ru.tanec.cookhelper.core.db.model.Categories
import ru.tanec.cookhelper.enterprise.model.entity.Category

class CategoryDaoImpl: CategoryDao {

    fun resultRowToCategory(row: ResultRow): Category = Category(
        id = row[Categories.id],
        title = row[Categories.title]
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

    override suspend fun getAll(): List<Category> = DatabaseFactory.dbQuery {

        Categories
            .selectAll()
            .map(::resultRowToCategory)
    }

    override suspend fun getAll(part: Int, div: Int): List<Category> = DatabaseFactory.dbQuery {
        Categories
            .selectAll()
            .map(::resultRowToCategory)
            .partOfDiv(part, div)
    }

    override suspend fun getById(id: Long): Category? = DatabaseFactory.dbQuery {
        Categories
            .select { Categories.id eq id }
            .map(::resultRowToCategory)
            .singleOrNull()
    }
}