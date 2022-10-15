package ru.tanec.cookhelper.database.dao.commentDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.Comments
import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment

class CommentDaoImpl: CommentDao {

    private fun resultRowToComment(row: ResultRow) = Comment(
        id = row[Comments.id],
        authorId = row[Comments.authorId],
        text = row[Comments.text],
        timestamp = row[Comments.timestamp]
    )

    override suspend fun getById(id: Long): Comment? = dbQuery {
        Comments
            .select{ Comments.id eq id}
            .map(::resultRowToComment)
            .singleOrNull()
    }

    override suspend fun getByIdList(list: List<Long>): List<Comment> = dbQuery {
        val data: MutableList<Comment> = mutableListOf()
        var comment: Comment?
        list.forEach { id ->
            comment = Comments.select{ Comments.id eq id}.map(::resultRowToComment).singleOrNull()
            if (comment != null) data.add(comment!!)
        }
        data.toList()
    }

    override suspend fun insert(comment: Comment): Comment? = dbQuery {
        Comments
            .insert {
                it[authorId] = comment.authorId
                it[text] = comment.text
                it[timestamp] = comment.timestamp
            }

        Comments
            .select { Comments.timestamp eq comment.timestamp}
            .map(::resultRowToComment)
            .singleOrNull()
    }
}