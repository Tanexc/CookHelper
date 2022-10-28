package ru.tanec.cookhelper.database.dao.userDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.core.utils.FileController.toFileData
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
        avatar = row[Users.avatar].split(FILE_DELIMITER).map{it.toFileData(userDataFolder)},
        images = row[Users.images].split(FILE_DELIMITER).map{it.toFileData(userDataFolder)},
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
