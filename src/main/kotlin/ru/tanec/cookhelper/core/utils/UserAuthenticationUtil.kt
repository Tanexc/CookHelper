package ru.tanec.cookhelper.core.utils

import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

suspend fun checkUserToken(repository: UserRepository, token: String): User? {

    return when (val stateUser = repository.getByToken(token).last()) {
        is State.Success -> stateUser.data
        else -> null

    }
}