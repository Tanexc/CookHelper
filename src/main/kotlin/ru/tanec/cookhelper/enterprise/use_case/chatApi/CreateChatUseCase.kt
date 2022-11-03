package ru.tanec.cookhelper.enterprise.use_case.chatApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.CHAT_NOT_CREATED
import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.core.utils.getChatOrNull
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object CreateChatUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<Chat?> {
        try {
            var chat = parameters.getChatOrNull(userRepository)?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = "error",
                data = null
            )

            when(val r = repository.insert(chat).last()) {
                is State.Error -> return ApiResponse(
                    status = CHAT_NOT_CREATED,
                    message = "error",
                    data = null
                )
                is State.Success -> {
                    r.data?.let {chat = r.data}
                }

                else -> {}
            }

            var state: State<User?>
            for (userId in chat.members) {
                state = userRepository.getById(userId).last()
                if (state.data != null) {
                    userWebsocketConnectionController.updateData(state.data!!, userRepository)
                    userRepository.edit(state.data!!.copy(chats=(state.data!!.chats + listOf(chat.id))))
                }
            }

            return ApiResponse(status = SUCCESS, message = "success", data = chat)

        } catch (e: Exception) {
            return ApiResponse(
                status = EXCEPTION,
                message = e.message ?: "exception in CreateUseCase",
                data = null
            )
        }
    }

}