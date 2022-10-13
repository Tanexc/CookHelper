package ru.tanec.cookhelper.enterprise.repository

import kotlinx.coroutines.flow.Flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.db.dao.postDao.PostDao
import ru.tanec.cookhelper.core.db.dao.recipeDao.RecipeDao
import ru.tanec.cookhelper.core.db.dao.recipeDao.RecipeDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.Post

interface PostRepository {

    val dao: PostDao

    fun insert(post: Post): Flow<State<Post?>>

    fun getById(id: Long): Flow<State<Post?>>

    fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Post>>>

    fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Post>>>

    fun editPost(post: Post): Flow<State<Post>>

    fun deletePost(post: Post): Flow<State<Any>>
}