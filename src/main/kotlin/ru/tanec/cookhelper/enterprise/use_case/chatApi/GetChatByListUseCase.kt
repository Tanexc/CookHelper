package ru.tanec.cookhelper.enterprise.use_case.chatApi

import io.ktor.http.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.REQUIRED
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object GetChatByListUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: Parameters,
    ): ApiResponse<List<ChatResponseData>?> {

        val token = parameters["token"]?.filter { it != '"' } ?: return ApiResponse(
            status = PARAMETER_MISSED,
            message = REQUIRED("token"),
            data = null
        )
        val ids = parameters["listId"]?.filter { it != '"' }?.split("*")?.mapNotNull { it.toLongOrNull() }
            ?: return ApiResponse(
                status = PARAMETER_MISSED,
                message = REQUIRED("listId"),
                data = null
            )

        val user = checkUserToken(userRepository, token) ?: return ApiResponse(
            status = USER_TOKEN_INVALID,
            message = "error",
            data = null
        )

        userWebsocketConnectionController.updateData(user, userRepository)

        val limit = parameters["limit"]?.filter { it != '"' }?.toIntOrNull()

        val offset = parameters["offset"]?.filter { it != '"' }?.toIntOrNull()

        val chats = repository.getChatByIdList(ids, limit, offset).last().data

        chats?.filter { it.members.contains(user.id) }

        return ApiResponse(status = SUCCESS, message = "success", chats?.map { ch ->
            ch.asResponseData(
                messages = emptyList(),
                lastMessage = repository.getMessages(repository.getChatMessages(ch.id, 0, 3) ?: listOf())
                    .maxByOrNull { it.timestamp },
                member = userRepository.getById(ch.members.first { it != user.id }).last().data?.smallInfo()?: user.smallInfo(),
                newMessagesCount = repository.getMessages(ch.messages).filter { !it.views.contains(user.id) }.size
                )
        })

    }
}