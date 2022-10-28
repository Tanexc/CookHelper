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

object CreateChatUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Chat?> {
        try {
            val chat = parameters.getChatOrNull(userRepository)?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = "error",
                data = null
            )

            when(repository.insert(chat).last()) {
                is State.Error -> return ApiResponse(
                    status = CHAT_NOT_CREATED,
                    message = "error",
                    data = null
                )
                else -> {}
            }

            var state: State<User?>
            for (userId in chat.members) {
                state = userRepository.getById(userId).last()
                if (state.data != null) {
                    userRepository.editChats(state.data!!.copy(chats=(state.data!!.chats + mutableListOf(chat.id)) as MutableList<Long>))
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