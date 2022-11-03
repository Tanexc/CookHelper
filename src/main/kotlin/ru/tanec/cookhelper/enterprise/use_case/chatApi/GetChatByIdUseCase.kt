package ru.tanec.cookhelper.enterprise.use_case.chatApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.MessageRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetChatByIdUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        messageRepository: MessageRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters,
    ): ApiResponse<ChatResponseData?> {

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

        userWebsocketConnectionController.updateData(user, userRepository)

        val chat = repository.getChatById(id).last().data?: return ApiResponse(
            status = CHAT_NOT_FOUND,
            message = "error",
            data = null
        )

        if (!chat.members.contains(user.id)) return ApiResponse(
            status= PERMISSION_DENIED,
            message="error",
            data=null
        )

        val messages = repository.getChatMessages(chat.id, 0, 200)?.let { messageRepository.getByListId(it) } ?: emptyList()

        val allMessages = messageRepository.getByListId(chat.messages)

        val members = chat.members.toMutableList()
        members.remove(user.id)

        val chatData: ChatResponseData = chat.asResponseData(
            messages=messages.sortedBy { it.timestamp }.map { it.asResponseData(userRepository.getById(it.authorId).last().data ?: user)},
            lastMessage = messages.maxByOrNull { it.timestamp },
            member = userRepository.getById(members.firstOrNull()?: user.id).last().data?.smallInfo()?:user.smallInfo(),
            newMessagesCount = allMessages.filter { !it.views.contains(user.id) }.size
        )

        return ApiResponse(status = SUCCESS, message="success", chatData)

    }
}