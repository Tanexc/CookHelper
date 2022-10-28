package ru.tanec.cookhelper.database.dao.postDao

import org.jetbrains.exposed.sql.*
import ru.tanec.cookhelper.core.constants.FILE_DELIMITER
import ru.tanec.cookhelper.core.constants.SECOND_DELIMITER
import ru.tanec.cookhelper.core.constants.SEPORATOR
import ru.tanec.cookhelper.core.utils.FileController
import ru.tanec.cookhelper.core.utils.partOfDiv
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Posts
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import ru.tanec.cookhelper.enterprise.model.entity.post.Post

class PostDaoImpl : PostDao {

    private fun resultRowToPost(row: ResultRow) = Post(
        id = row[Posts.id],
        authorId = row[Posts.authorId],
        text = row[Posts.text],
        label = row[Posts.label],
        attachments = row[Posts.attachments].split(FILE_DELIMITER),
        images = row[Posts.images].split(FILE_DELIMITER).map { it.toFile(feedDataFolder) },
        comments = row[Posts.comments].split(" "),
        reposts = row[Posts.reposts].split(" ").mapNotNull { it.toLongOrNull() },
        likes = row[Posts.likes].split(" ").mapNotNull { it.toLongOrNull() },
        timestamp = row[Posts.timestamp]

    )

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

    override suspend fun getByUser(userId: Long, part: Int?, div: Int?): List<Post> = dbQuery {
        Posts
            .select { Posts.authorId eq userId }
            .map(::resultRowToPost)
            .partOfDiv(part, div)
    }

    override suspend fun insertPost(post: Post): Post = dbQuery {
        Posts
            .insert { row ->
                row[authorId] = post.authorId?: 0
                row[text] = post.text
                row[label] = post.label
                row[attachments] = post.attachments.joinToString(SEPORATOR)
                row[images] = post.images.joinToString(FILE_DELIMITER) { it.id }
                row[likes] = post.likes.joinToString(" ")
                row[reposts] = post.reposts.joinToString(" ")
                row[comments] = post.comments.joinToString(SECOND_DELIMITER)
                row[timestamp] = post.timestamp

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
                it[label] = post.label
                it[attachments] = post.attachments.joinToString(FILE_DELIMITER)
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
            .deleteWhere { (Posts.id eq post.id) and (Posts.authorId eq post.authorId!!)  }
    }

    override suspend fun getByList(listId: List<Long>, part: Int?, div: Int?): List<Post> {
        val data: MutableList<Post> = mutableListOf()
        for (id: Long in if (part != null && div != null ) listId.partOfDiv(part, div) else listId) {
            getById(id)?.let { data.add(it) }
        }
        return data.toList()
    }
}