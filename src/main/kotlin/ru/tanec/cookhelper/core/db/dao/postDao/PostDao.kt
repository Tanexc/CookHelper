package ru.tanec.cookhelper.core.db.dao.postDao

import ru.tanec.cookhelper.enterprise.model.entity.Post

interface PostDao {

    suspend fun getAll(): List<Post>

    suspend fun getAll(part: Int, div: Int): List<Post>

    suspend fun getById(id: Long): Post?

    suspend fun getByUser(userId: Long, part: Int, div: Int): List<Post>

    suspend fun insertPost(recipe: Post): Post

    suspend fun editRecipe(post: Post): Post

    suspend fun deletePost(post: Post)

}