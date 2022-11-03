package ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller

import io.ktor.http.*
import io.ktor.server.websocket.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

class UserWebsocketConnectionController {

    private val data: MutableMap<Long, MutableSharedFlow<User>> = mutableMapOf()

    suspend fun connect(
        parameters: Parameters,
        userRepository: UserRepository
    ): Flow<State<Pair<Boolean, MutableSharedFlow<User>?>?>> = flow {

        val id: Long? = parameters["id"]?.toLongOrNull()
        val token: String? = parameters["token"]

        when (id == null || token == null) {

            true -> emit(State.Error(data = null, message = "error", status = PARAMETER_MISSED))

            else -> {
                val provider = checkUserToken(userRepository, token)

                val user = userRepository.getById(id).last().data

                if (provider == null) {
                    emit(State.Error(data = null, message = "error", status = USER_TOKEN_INVALID))
                } else if (user == null) {
                    emit(State.Error(data = null, message = "error", status = USER_NOT_FOUND))
                } else if (user.id == provider.id) {
                    if (data[user.id] == null) data[user.id] = MutableSharedFlow()
                    emit(State.Success(Pair(true, data[user.id])))
                } else {
                    emit(State.Success(Pair(false, data[user.id])))
                }
            }
        }
    }

    suspend fun sendMessage(
        session: DefaultWebSocketServerSession,
        response: WebsocketResponse<User?>
    ) = session.sendSerialized(response)

    suspend fun updateData(user: User, userRepository: UserRepository) {
        val userToUpdate = userRepository.getFullUser(user).last()?: return
        val u = userRepository.edit(userToUpdate.copy(lastSeen = getTimeMillis())).last().data?: user
        if (data[u.id] == null) data[u.id] = MutableSharedFlow()
        data[u.id]!!.emit(u)
    }
}