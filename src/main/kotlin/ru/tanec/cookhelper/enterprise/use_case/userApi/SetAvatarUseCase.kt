package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.USER_NOT_FOUND
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.database.utils.FileController
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object SetAvatarUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: List<PartData>,
    ): ApiResponse<User?> {

        var token: String? = null
        var avatar: PartData.FileItem? = null

        parameters.forEach {
            when (it) {
                is PartData.FormItem -> {
                    token = if (it.name == "token") it.value else null
                }

                is PartData.FileItem -> {
                    if (it.contentType != null) {
                        avatar = it
                    }
                }

                else -> {}
            }
        }

        println(avatar?.contentType?.contentSubtype)

        val user = checkUserToken(repository, token ?: "") ?: return State.Error<User>(status= USER_NOT_FOUND).asApiResponse()

        val fileList =
            avatar?.let { listOf(FileController.uploadFile(userDataFolder, it, it.contentType!!.contentType)) }
                ?: listOf()

        return repository.edit(user.copy(avatar = user.avatar + fileList)).last().asApiResponse()


    }
}