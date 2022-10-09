package ru.tanec.cookhelper.core.db.dao.userDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.db.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.core.db.model.Users
import ru.tanec.cookhelper.enterprise.model.entity.User

class UserDaoImpl : UserDao {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        surname = row[Users.surname],
        nickname = row[Users.nickname],
        email = row[Users.email],
        password = row[Users.password],
        avatar = row[Users.avatar].split(" ").toMutableList(),
        lastSeen = row[Users.lastSeen],
        status = row[Users.status],
        deleted = row[Users.deleted],
        verified = row[Users.verified],
        code = row[Users.code],
        recoveryCode = row[Users.recoveryCode],
        token = row[Users.token],

        fridge = row[Users.fridge].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        topics = row[Users.topics].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        starredRecipes = row[Users.starredRecipes].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        bannedRecipes = row[Users.bannedRecipes].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        starredIngredients = row[Users.starredIngredients].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        bannedIngredients = row[Users.bannedIngredients].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        chats = row[Users.chats].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        userRecipes = row[Users.userRecipes].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        userPosts = row[Users.userPosts].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        subscribes = row[Users.subscribes].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        subscribers = row[Users.subscribers].split(" ").mapNotNull { it.toLongOrNull() }.toMutableList(),
        registrationTimestamp = row[Users.registrationTimestamp]

    )

    override suspend fun getAll(): MutableList<User> = dbQuery {
        Users
            .selectAll()
            .map(::resultRowToUser)
            .toMutableList()
    }

    override suspend fun getById(id: Long): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addNew(user: User): User? = dbQuery {
        Users
            .insert {
                it[name] = user.name
                it[surname] = user.surname
                it[nickname] = user.nickname
                it[email] = user.email
                it[password] = user.getPsw()
                it[avatar] = user.avatar.joinToString(" ")
                it[lastSeen] = user.lastSeen
                it[status] = user.status
                it[deleted] = user.deleted
                it[verified] = user.verified
                it[code] = user.code
                it[recoveryCode] = user.recoveryCode
                it[token] = user.token
                it[fridge] = user.fridge.joinToString(" ")
                it[topics] = user.topics.joinToString(" ")
                it[starredRecipes] = user.starredRecipes.joinToString(" ")
                it[bannedRecipes] = user.bannedRecipes.joinToString(" ")
                it[starredIngredients] = user.starredIngredients.joinToString(" ")
                it[bannedIngredients] = user.bannedIngredients.joinToString(" ")
                it[chats] = user.chats.joinToString(" ")
                it[userRecipes] = user.userRecipes.joinToString(" ")
                it[userPosts] = user.userPosts.joinToString(" ")
                it[subscribes] = user.subscribes.joinToString(" ")
                it[subscribers] = user.subscribers.joinToString(" ")
                it[registrationTimestamp] = user.registrationTimestamp!!
            }

        Users
            .select { Users.nickname eq user.nickname }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun editUser(user: User): User = dbQuery {
        Users
            .update({ Users.id eq user.id }) {
                it[id] = user.id
                it[name] = user.name
                it[surname] = user.surname
                it[nickname] = user.nickname
                it[email] = user.email
                it[password] = user.getPsw()
                it[avatar] = user.avatar.joinToString(" ")
                it[lastSeen] = user.lastSeen
                it[status] = user.status
                it[deleted] = user.deleted
                it[verified] = user.verified
                it[code] = user.code
                it[recoveryCode] = user.recoveryCode
                it[token] = user.token
                it[fridge] = user.fridge.joinToString(" ")
                it[topics] = user.topics.joinToString(" ")
                it[starredRecipes] = user.starredRecipes.joinToString(" ")
                it[bannedRecipes] = user.bannedRecipes.joinToString(" ")
                it[starredIngredients] = user.starredIngredients.joinToString(" ")
                it[bannedIngredients] = user.bannedIngredients.joinToString(" ")
                it[chats] = user.chats.joinToString(" ")
                it[userRecipes] = user.userRecipes.joinToString(" ")
                it[userPosts] = user.userPosts.joinToString(" ")
                it[subscribes] = user.subscribes.joinToString(" ")
                it[subscribers] = user.subscribers.joinToString(" ")
            }
        user
    }

    override suspend fun deleteById(id: Long): Boolean = dbQuery {

        Users
            .deleteWhere { Users.id eq id }

        true
    }

    override suspend fun getByLogin(login: String): MutableList<User> = dbQuery {
        Users
            .select { (Users.nickname eq login) or (Users.email eq login) }
            .map(::resultRowToUser)
            .toMutableList()
    }

    override suspend fun getByToken(token: String): User? = dbQuery {
        Users
            .select { Users.token eq token }
            .map(::resultRowToUser)
            .toMutableList()
            .singleOrNull()
    }
}
