package ru.tanec.cookhelper.presentation.features.api.chatApi.use_case

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.chatDataFolder
import ru.tanec.cookhelper.core.constants.status.CHAT_NOT_CREATED
import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.core.utils.FileController
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.chat.Chat
import ru.tanec.cookhelper.enterprise.model.entity.chat.ChatData
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.ChatReceiveData
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

            return ApiResponse(status = SUCCESS, message = "success", data=chat)

        } catch (e: Exception) {
            return ApiResponse(
                status = EXCEPTION,
                message = e.message ?: "exception in CreateUseCase",
                data = null
            )
        }
    }

}

private suspend fun List<PartData>.getChatOrNull(repo: UserRepository): Chat? {
    val data = ChatData()
    var user: User? = null
    this.forEach { part ->
        when (part.name) {
            "token" -> user = if (part is PartData.FormItem) checkUserToken(repo, part.value.filter{it!='"'}) else null
            "members" -> data.members =
                if (part is PartData.FormItem) part.value.filter{it!='"'}.split("*").mapNotNull { it.toLongOrNull() }
                    .toList() else emptyList()

            "avatar" -> {
                if (part is PartData.FileItem) {
                    val file = FileController.uploadFile(chatDataFolder, part)
                    data.avatar = listOf(file.id)
                }
            }
        }
    }
    return if (user == null) null else data.asDomain()
}