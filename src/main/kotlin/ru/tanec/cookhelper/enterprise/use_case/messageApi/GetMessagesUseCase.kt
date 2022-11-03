package ru.tanec.cookhelper.enterprise.use_case.messageApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.chat.Message
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetMessagesUseCase {

    suspend operator fun invoke(
        repository: MessageRepository,
        userRepository: UserRepository,
        chatRepository: ChatRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters
    ): ApiResponse<List<Message>?> {
        try {

            val token = parameters["token"]?.filter { it != '"' } ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("token"),
                data = null
            )
            val offset = parameters["offset"]?.filter { it != '"' }?.toIntOrNull() ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("offset"),
                data = null
            )

            val limit = parameters["limit"]?.filter { it != '"' }?.toIntOrNull() ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("limit"),
                data = null
            )

            val chatId = parameters["id"]?.filter { it != '"' }?.toLongOrNull() ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("id"),
                data = null
            )

            val user = checkUserToken(userRepository, token) ?: return ApiResponse(
                status = USER_TOKEN_INVALID,
                message = "error",
                data = null
            )

            userWebsocketConnectionController.updateData(user, userRepository)

            val chat = chatRepository.getChatById(chatId).last().data ?: return ApiResponse(
                status = CHAT_NOT_FOUND,
                message = "error",
                data = null
            )

            if (!chat.members.contains(user.id)) {
                return ApiResponse(
                    status = PERMISSION_DENIED,
                    message = "error",
                    data = null
                )
            }

            val messages = repository.getByOffset(chat.messages, limit, offset).last().data?: return ApiResponse(
                status = MESSAGE_NOT_FOUND,
                message = "failed to get messages",
                data = null
            )

            return ApiResponse(
                status = SUCCESS,
                message = "success",
                data = messages
            )

        } catch (e: Exception) {
            return ApiResponse(
                status = EXCEPTION,
                message = e.message ?: "exception in CreateUseCase",
                data = null
            )
        }
    }
}