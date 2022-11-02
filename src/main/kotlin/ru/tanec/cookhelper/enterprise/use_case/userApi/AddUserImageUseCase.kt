package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.userDataFolder
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.database.utils.FileController
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object AddUserImageUseCase {
    suspend operator fun invoke(
        repository: UserRepository,
        parameters: List<PartData>,
    ): ApiResponse<User?> {

        var token: String? = null
        var image: PartData.FileItem? = null

        parameters.forEach {
            when (it) {
                is PartData.FormItem -> {
                    token = if (it.value == "token") it.value else null
                }

                is PartData.FileItem -> {
                    if (it.contentType != null) {
                        image = it
                    }
                }

                else -> {}
            }
        }

        val user = checkUserToken(repository, token ?: "") ?: return State.Error<User>().asApiResponse()

        val fileList =
            image?.let { listOf(FileController.uploadFile(userDataFolder, it, it.contentType!!.contentType)) }
                ?: listOf()

        return repository.edit(user.copy(images = user.images + fileList)).last().asApiResponse()


    }
}