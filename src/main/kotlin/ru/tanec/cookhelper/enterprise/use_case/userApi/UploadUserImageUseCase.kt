package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.database.utils.FileController
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object UploadUserImageUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>,
    ): ApiResponse<User?> {

        var token: String? = null
        var image: PartData.FileItem? = null

        parameters.forEach {
            when (it) {
                is PartData.FormItem -> {
                    token = if (it.name == "token") it.value else null
                }

                is PartData.FileItem -> {
                    if (it.contentType != null) {
                        image = it
                    }
                }

                else -> {}
            }
        }

        var user = checkUserToken(repository, token ?: "") ?: return State.Error<User>(status= USER_NOT_FOUND).asApiResponse()

        val fileList =
            image?.let { listOf(FileController.uploadFile(userDataFolder, it, "${it.contentType!!.contentType}/${it.contentType!!.contentSubtype}")) }
                ?: listOf()

        user = user.copy(images = user.images + fileList)

        val state = repository.edit(user).last()

        user = state.data?: user

        userWebsocketConnectionController.updateData(user, repository)

        return state.asApiResponse(user.privateInfo())
    }
}