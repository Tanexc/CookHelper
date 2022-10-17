package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.SOME_ERROR
import ru.tanec.cookhelper.core.constants.SUCCESS
import ru.tanec.cookhelper.core.constants.status.PostStatus
import ru.tanec.cookhelper.database.dao.postDao.PostDao
import ru.tanec.cookhelper.database.dao.postDao.PostDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository

class PostRepositoryImpl(
    override val dao: PostDao = PostDaoImpl()
): PostRepository {
    override fun insert(post: Post): Flow<State<Post?>> = flow {
        emit(State.Processing())
        try {
            val data = dao.insertPost(post)
            emit(State.Success(message= SUCCESS, status= PostStatus.SUCCESS, data=data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: SOME_ERROR, status = PostStatus.EXCEPTION))
        }
    }

    override fun getById(id: Long): Flow<State<Post?>> = flow {
        emit(State.Processing())
        when (val post = dao.getById(id)) {
            is Post -> emit(State.Success(post, status = PostStatus.SUCCESS))
            else -> emit(State.Error(status = PostStatus.POST_NOT_FOUND))
        }
    }

    override fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Post>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getAll(part, div), status = PostStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = PostStatus.EXCEPTION))
        }

    }

    override fun getByUser(userId: Long, part: Int, div: Int): Flow<State<List<Post>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByUser(userId, part, div), status = PostStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = PostStatus.EXCEPTION))
        }
    }

    override fun editPost(post: Post): Flow<State<Post>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.editRecipe(post), status = PostStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = PostStatus.EXCEPTION))
        }
    }

    override fun deletePost(post: Post): Flow<State<Any>>  = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.deletePost(post), status = PostStatus.SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = PostStatus.EXCEPTION))
        }
    }
}