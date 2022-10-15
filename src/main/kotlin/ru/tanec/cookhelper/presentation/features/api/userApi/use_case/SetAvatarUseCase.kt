package ru.tanec.cookhelper.presentation.features.api.userApi.use_case

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.UserStatus
import ru.tanec.cookhelper.core.utils.FileController
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.UserRepository

object SetAvatarUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        multipartData: List<PartData>
    ): ApiResponse<User> {
        var avatarPart: PartData.FileItem? = null
        var user: User? = null
        var token = ""

        multipartData.forEach { part ->
            when (part) {
                is PartData.FormItem -> {
                    if (part.name == "token") {
                        user = repository.getByToken(part.value).last().data; token = part.value
                    }
                }

                is PartData.FileItem -> {
                    avatarPart = part
                }

                else -> {}
            }
        }

        return when ((avatarPart != null) and (user != null)) {
            true -> {
                val uniqueName = FileController.uploadUserFile(avatarPart!!, user = user!!.id)
                val state = repository.addAvatar(token = token, uniqueName).last()
                ApiResponse(state.status, state.message, state.data)
            }

            false -> {
                ApiResponse(UserStatus.UPLOAD_FAILED, "upload failed", null)
            }
        }
    }
}