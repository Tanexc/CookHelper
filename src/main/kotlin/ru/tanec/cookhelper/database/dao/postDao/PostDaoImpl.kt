package ru.tanec.cookhelper.database.dao.postDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.SECOND_DELIMITER
import ru.tanec.cookhelper.core.constants.SEPORATOR
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Posts
import ru.tanec.cookhelper.enterprise.model.entity.post.Post

class PostDaoImpl : PostDao {

    private fun resultRowToPost(row: ResultRow) = Post(
        id = row[Posts.id],
        authorId = row[Posts.authorId],
        text = row[Posts.text],
        attachment = row[Posts.attachment].split(FILE_DELIMITER),
        images = row[Posts.images].split(FILE_DELIMITER),
        comments = row[Posts.comments].split(" "),
        reposts = row[Posts.reposts].split(" ").mapNotNull { it.toLongOrNull() },
        likes = row[Posts.likes].split(" ").mapNotNull { it.toLongOrNull() },
        timestamp = row[Posts.timestamp]

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

    override suspend fun getAll(): List<Post> = dbQuery {
        Posts
            .selectAll()
            .map(::resultRowToPost)

    }

    override suspend fun getAll(part: Int, div: Int): List<Post> = dbQuery {
        Posts
            .selectAll()
            .map(::resultRowToPost)
            .partOfDiv(part, div)
    }


    override suspend fun getById(id: Long): Post? = dbQuery {
        Posts
            .select { Posts.id eq id }
            .map(::resultRowToPost)
            .singleOrNull()
    }

    override suspend fun getByUser(userId: Long, part: Int, div: Int): List<Post> = dbQuery {
        Posts
            .select { Posts.authorId eq userId }
            .map(::resultRowToPost)
            .partOfDiv(part, div)
    }

    override suspend fun insertPost(post: Post): Post = dbQuery {
        Posts
            .insert {
                it[authorId] = post.authorId
                it[text] = post.text
                it[attachment] = post.attachment.joinToString(SEPORATOR)
                it[images] = post.images.joinToString(FILE_DELIMITER)
                it[likes] = post.likes.joinToString(" ")
                it[reposts] = post.reposts.joinToString(" ")
                it[comments] = post.comments.joinToString(SECOND_DELIMITER)
                it[timestamp] = post.timestamp

            }

        Posts
            .select { Posts.timestamp eq post.timestamp }
            .map(::resultRowToPost)
            .singleOrNull() ?: post

    }

    override suspend fun editRecipe(post: Post): Post = dbQuery {
        Posts
            .update {
                it[text] = post.text
                it[attachment] = post.attachment.joinToString(FILE_DELIMITER)
                it[images] = post.images.joinToString(FILE_DELIMITER)
                it[likes] = post.likes.joinToString(" ")
                it[reposts] = post.reposts.joinToString(" ")
                it[comments] = post.comments.joinToString(SECOND_DELIMITER)
            }
        Posts
            .select { Posts.id eq post.id }
            .map(::resultRowToPost)
            .singleOrNull() ?: post
    }

    override suspend fun deletePost(post: Post) {
        Posts
            .deleteWhere { (Posts.id eq post.id) and (Posts.authorId eq post.authorId)  }
    }
}