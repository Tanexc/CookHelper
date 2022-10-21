package ru.tanec.cookhelper.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.SOME_ERROR
import ru.tanec.cookhelper.core.constants.status.*
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
            emit(State.Success(data=data))
        } catch (e: Exception) {
            emit(State.Error(message = e.message ?: SOME_ERROR, status = EXCEPTION))
        }
    }

    override fun getById(id: Long): Flow<State<Post?>> = flow {
        emit(State.Processing())
        when (val post = dao.getById(id)) {
            is Post -> emit(State.Success(post))
            else -> emit(State.Error(status = POST_NOT_FOUND))
        }
    }

    override fun getAll(id: Long, part: Int, div: Int): Flow<State<List<Post>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getAll(part, div)))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }

    }

    override fun getByUser(userId: Long, part: Int?, div: Int?): Flow<State<List<Post>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByUser(userId, part, div), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun getByList(id: List<Long>, part: Int?, div: Int?): Flow<State<List<Post>>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.getByList(id, part, div), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun editPost(post: Post): Flow<State<Post>> = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.editRecipe(post), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }

    override fun deletePost(post: Post): Flow<State<Any>>  = flow {
        emit(State.Processing())
        try {
            emit(State.Success(dao.deletePost(post), status = SUCCESS))

        } catch (e: Exception) {
            emit(State.Error(status = EXCEPTION))
        }
    }
}