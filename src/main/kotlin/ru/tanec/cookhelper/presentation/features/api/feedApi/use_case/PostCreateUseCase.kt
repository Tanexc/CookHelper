package ru.tanec.cookhelper.presentation.features.api.feedApi.use_case

import io.ktor.http.content.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.constants.status.UserStatus
import ru.tanec.cookhelper.core.utils.FileController.uploadPostFile
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.receive.postApi.PostData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.PostRepository
import ru.tanec.cookhelper.enterprise.repository.UserRepository

object PostCreateUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Post> {

        val state = when (val post = fromMultipart(parameters, userRepository)?.asDomain()) {
            null -> {
                State.Error(status = RecipeStatus.PARAMETER_MISSED)
            }

            else -> {
                if (post.authorId != null) {
                repository.insert(
                    post
                ).last()}
                else State.Error(status = UserStatus.USER_NOT_FOUND)
            }
        }

        return ApiResponse(state.status, state.message, state.data)
    }


    private suspend fun fromMultipart(partList: List<PartData>, userRepository: UserRepository): PostData? {
        var token: String? = null
        val authorId: Long?
        var text: String? = null
        val attachment: List<PartData.FileItem> = listOf()
        var images: List<PartData.FileItem> = listOf()

        partList.forEach { pt ->
            when (pt) {
                is PartData.FormItem -> {
                    when (pt.name) {
                        "token" -> {
                            token = pt.value
                        }

                        "text" -> {
                            text = pt.value
                        }

                        else -> {}
                    }
                }

                //TODO("attachment")

                is PartData.FileItem -> {

                    images = images + listOf(pt)
                }

                else -> {}
            }

        }

        val data = userRepository.getByToken(token?:"").last()

        authorId = when(data) {
            is State.Success -> data.data?.id
            else -> null
        }

        return if ((token != null) and (text != null)) PostData(
            authorId,
            text,
            attachment,
            images
        ) else null


    }

    private fun PostData.asDomain(): Post {

        return Post(
            authorId = this.authorId,
            text = this.text ?: "",
            attachment = this.attachment.map { uploadPostFile(it, this.authorId ?: 0) },
            images = this.images.map { uploadPostFile(it, this.authorId ?: 0) },
            comments = listOf(),
            likes = listOf(),
            reposts = listOf(),
            timestamp = getTimeMillis()
        )
    }
}