package ru.tanec.cookhelper.database.factory

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.*
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import ru.tanec.cookhelper.database.model.*

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcURL = "jdbc:sqlite:file:./build/db.db"
        val database = Database.connect(jdbcURL, driverClassName)
        transaction(database) {
            SchemaUtils.create(Users)
            SchemaUtils.create(Recipes)
            SchemaUtils.create(Posts)
            SchemaUtils.create(Categories)
            SchemaUtils.create(Ingredients)
            SchemaUtils.create(Comments)
            SchemaUtils.create(Topics)
            SchemaUtils.create(Attachments)
            SchemaUtils.create(Messages)
            SchemaUtils.create(Chats)
            SchemaUtils.create(Answers)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

