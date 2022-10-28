package ru.tanec.cookhelper.enterprise.use_case.chatApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object GetChatUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        messageRepository: MessageRepository,
        parameters: Parameters
    ): ApiResponse<ChatResponseData?> {
        try {

            val token = parameters["token"]?.filter { it != '"' } ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("token"),
                data = null
            )
            val id = parameters["id"]?.filter { it != '"' }?.toLongOrNull() ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("id"),
                data = null
            )

            val user = checkUserToken(userRepository, token) ?: return ApiResponse(
                status = USER_TOKEN_INVALID,
                message = "error",
                data = null
            )

            val chat = repository.getChatById(id).last().data ?: return ApiResponse(
                status = CHAT_NOT_FOUND,
                message = "error",
                data = null
            )

            return if (user.id in chat.members) {
                val listMes: List<Message> = messageRepository.getByIdList(chat.messages, 1, 2).last().data?: emptyList()
                ApiResponse(
                    data = ChatResponseData(
                        id = chat.id,
                        title = chat.title,
                        members = chat.members,
                        messages = listMes,
                        attachments = chat.attachments,
                        avatar = chat.avatar,
                        creationTimestamp = chat.creationTimestamp
                    ), status = SUCCESS, message = "success"
                )
            } else {
                ApiResponse(data = null, status = PERMISSION_DENIED, message = "permission denied")
            }


        } catch (e: Exception) {
            return ApiResponse(
                status = EXCEPTION,
                message = e.message ?: "exception in CreateUseCase",
                data = null
            )
        }
    }
}