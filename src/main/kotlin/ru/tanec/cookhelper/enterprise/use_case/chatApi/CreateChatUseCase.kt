package ru.tanec.cookhelper.enterprise.use_case.chatApi

import io.ktor.http.*
import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.getChatOrNull
import ru.tanec.cookhelper.database.utils.FileController.uploadFile
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.ChatData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.ChatResponseData
import ru.tanec.cookhelper.enterprise.model.response.TopicResponseData
import ru.tanec.cookhelper.enterprise.repository.api.ChatRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object CreateChatUseCase {
    suspend operator fun invoke(
        repository: ChatRepository,
        userRepository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<ChatResponseData?> {
        try {

            var token: String? = null
            val chatData = ChatData()

            parameters.filterIsInstance<PartData.FormItem>().forEach {parameter ->
                when (parameter.name) {
                    "token" -> token = parameter.value
                    "members" -> chatData.members = parameter.value.split("*").mapNotNull{ it.toLongOrNull() }
                    "title" -> chatData.title = parameter.value
                }
            }

            val userState = userRepository.getByToken(token ?: "").last()

            var user = userState.data ?: return State.Error<ChatResponseData?>(status = USER_NOT_FOUND).asApiResponse()



            parameters.filterIsInstance<PartData.FileItem>().forEach {parameter ->
                chatData.avatar = chatData.avatar.plus(uploadFile(chatDataFolder, parameter, "${parameter.contentType?.contentType}/${parameter.contentType?.contentSubtype}"))
            }

            return when(val chat = chatData.asDomain()) {
                is Chat -> {

                    val member = userRepository.getById(chat.members.firstOrNull { it != user.id } ?: user.id).last().data?: user

                    val chatResponseData = chat.asResponseData(member.smallInfo(), null, 0, emptyList())

                    State.Success<ChatResponseData?>(data=chatResponseData).asApiResponse()

                }

                else -> State.Error<ChatResponseData?>(status = PARAMETER_MISSED).asApiResponse()
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