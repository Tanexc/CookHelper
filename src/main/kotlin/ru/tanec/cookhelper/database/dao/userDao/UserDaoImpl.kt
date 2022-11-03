package ru.tanec.cookhelper.database.dao.userDao

import io.ktor.util.date.*
import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.database.utils.FileController.toFileData
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Users
import ru.tanec.cookhelper.enterprise.model.entity.user.User

class UserDaoImpl : UserDao {

    private fun resultRowToUser(row: ResultRow) = User(
        id = row[Users.id],
        name = row[Users.name],
        surname = row[Users.surname],
        nickname = row[Users.nickname],
        email = row[Users.email],
        password = row[Users.password],
        avatar = row[Users.avatar].split(FILE_DELIMITER).mapNotNull{ toFileData(it)},
        images = row[Users.images].split(FILE_DELIMITER).mapNotNull{ toFileData(it)},
        lastSeen = row[Users.lastSeen],
        status = row[Users.status],
        deleted = row[Users.deleted],
        verified = row[Users.verified],
        code = row[Users.code],
        recoveryCode = row[Users.recoveryCode],
        token = row[Users.token],

        fridge = row[Users.fridge].split(" ").mapNotNull { it.toLongOrNull() },
        topics = row[Users.topics].split(" ").mapNotNull { it.toLongOrNull() },
        starredRecipes = row[Users.starredRecipes].split(" ").mapNotNull { it.toLongOrNull() },
        bannedRecipes = row[Users.bannedRecipes].split(" ").mapNotNull { it.toLongOrNull() },
        starredIngredients = row[Users.starredIngredients].split(" ").mapNotNull { it.toLongOrNull() },
        bannedIngredients = row[Users.bannedIngredients].split(" ").mapNotNull { it.toLongOrNull() },
        chats = row[Users.chats].split(" ").mapNotNull { it.toLongOrNull() },
        userRecipes = row[Users.userRecipes].split(" ").mapNotNull { it.toLongOrNull() },
        userPosts = row[Users.userPosts].split(" ").mapNotNull { it.toLongOrNull() },
        subscribes = row[Users.subscribes].split(" ").mapNotNull { it.toLongOrNull() },
        subscribers = row[Users.subscribers].split(" ").mapNotNull { it.toLongOrNull() },
        registrationTimestamp = row[Users.registrationTimestamp]

    )

    override suspend fun getAll(): List<User> = dbQuery {
        Users
            .selectAll()
            .map(::resultRowToUser)

    }

    override suspend fun getById(id: Long): User? = dbQuery {
        Users
            .select { Users.id eq id }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun addNew(user: User): User? = dbQuery {
        Users
            .insert { row ->
                row[name] = user.name
                row[surname] = user.surname
                row[nickname] = user.nickname
                row[email] = user.email
                row[password] = user.getPsw()
                row[avatar] = user.avatar.joinToString(" ")
                row[lastSeen] = user.lastSeen
                row[status] = user.status
                row[deleted] = user.deleted
                row[verified] = user.verified
                row[code] = user.code
                row[recoveryCode] = user.recoveryCode
                row[token] = user.token
                row[fridge] = user.fridge.joinToString(" ")
                row[topics] = user.topics.joinToString(" ")
                row[starredRecipes] = user.starredRecipes.joinToString(" ")
                row[bannedRecipes] = user.bannedRecipes.joinToString(" ")
                row[starredIngredients] = user.starredIngredients.joinToString(" ")
                row[bannedIngredients] = user.bannedIngredients.joinToString(" ")
                row[chats] = user.chats.joinToString(" ")
                row[userRecipes] = user.userRecipes.joinToString(" ")
                row[userPosts] = user.userPosts.joinToString(" ")
                row[subscribes] = user.subscribes.joinToString(" ")
                row[subscribers] = user.subscribers.joinToString(" ")
                row[registrationTimestamp] = user.registrationTimestamp?: getTimeMillis()
                row[images] = user.images.joinToString(FILE_DELIMITER) { it.name }
            }

        Users
            .select { Users.nickname eq user.nickname }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun editUser(user: User): User = dbQuery {
        Users
            .update({ Users.id eq user.id }) { row ->
                row[id] = user.id
                row[name] = user.name
                row[surname] = user.surname
                row[nickname] = user.nickname
                row[email] = user.email
                row[password] = user.getPsw()
                row[avatar] = user.avatar.joinToString(" ")
                row[lastSeen] = getTimeMillis()
                row[status] = user.status
                row[deleted] = user.deleted
                row[verified] = user.verified
                row[code] = user.code
                row[recoveryCode] = user.recoveryCode
                row[token] = user.token
                row[fridge] = user.fridge.joinToString(" ")
                row[topics] = user.topics.joinToString(" ")
                row[starredRecipes] = user.starredRecipes.joinToString(" ")
                row[bannedRecipes] = user.bannedRecipes.joinToString(" ")
                row[starredIngredients] = user.starredIngredients.joinToString(" ")
                row[bannedIngredients] = user.bannedIngredients.joinToString(" ")
                row[chats] = user.chats.joinToString(" ")
                row[userRecipes] = user.userRecipes.joinToString(" ")
                row[userPosts] = user.userPosts.joinToString(" ")
                row[subscribes] = user.subscribes.joinToString(" ")
                row[subscribers] = user.subscribers.joinToString(" ")
                row[images] = user.images.joinToString(FILE_DELIMITER) { it.name }
            }
        user
    }

    override suspend fun getFullUser(user: User): User? = dbQuery {
        Users
            .select {Users.id eq user.id}
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun deleteById(id: Long): Boolean = dbQuery {

        Users
            .deleteWhere { Users.id eq id }

        true
    }

    override suspend fun getByLogin(login: String): User? = dbQuery {
        Users
            .select { (Users.nickname eq login) or (Users.email eq login) }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getByToken(token: String): User? = dbQuery {
        Users
            .select { Users.token eq token }
            .map(::resultRowToUser)
            .singleOrNull()
    }

    override suspend fun getByNickname(nickname: String): List<User> = dbQuery {

        Users
            .select { Users.nickname like nickname }
            .map(::resultRowToUser)

    }
}
