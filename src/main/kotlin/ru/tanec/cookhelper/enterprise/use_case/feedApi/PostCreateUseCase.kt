package ru.tanec.cookhelper.enterprise.use_case.feedApi

import io.ktor.http.content.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.feedDataFolder
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.core.utils.FileController.uploadFile
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.receive.postApi.PostData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.PostRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository

object PostCreateUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        userRepository: UserRepository,
        parameters: List<PartData>
    ): ApiResponse<Post> {

        //TODO("send more info about user")

        //TODO fix comment when sending response

        val state = when (val post = fromMultipart(parameters, userRepository)?.asDomain()) {
            null -> {
                State.Error(status = PARAMETER_MISSED)
            }

            else -> {
                if (post.authorId != null) {
                    repository.insert(
                        post
                    ).last()
                } else State.Error(status = USER_NOT_FOUND)
            }
        }

        return ApiResponse(state.status, state.message, state.data)
    }


    private suspend fun fromMultipart(partList: List<PartData>, userRepository: UserRepository): PostData? {
        var token: String? = null
        val authorId: Long?
        var text: String? = null
        var label: String? = null
        val attachment: List<PartData.FileItem> = listOf()
        var images: List<PartData.FileItem> = listOf()

        partList.forEach { pt ->
            when (pt) {
                is PartData.FormItem -> {
                    when (pt.name) {
                        "token" -> {
                            println(pt.value)
                            token = pt.value.filter { it != '"' }
                        }

                        "text" -> {
                            println("text")
                            text = pt.value.filter { it != '"' }
                        }

                        "label" -> {
                            println("label")
                            label = pt.value.filter { it != '"' }
                        }

                        else -> {}
                    }
                }

                //TODO("attachment")

                is PartData.FileItem -> {
                    print("image")
                    images = images + listOf(pt)
                }

                else -> {}
            }

        }

        val data = userRepository.getByToken(token ?: "").last()

        authorId = when (data) {
            is State.Success -> data.data?.id
            else -> null
        }

        println(images.toString())

        println("author id  $authorId")

        return if ((authorId != null) and (text != null)) PostData(
            authorId,
            text,
            label,
            attachment,
            images
        ) else null


    }

    private fun PostData.asDomain(): Post {

        return Post(
            authorId = this.authorId,
            text = this.text ?: "",
            label = this.label ?: "",
            attachments = this.attachment.map { uploadFile(feedDataFolder, it)},
            comments = listOf(),
            likes = listOf(),
            reposts = listOf(),
            timestamp = getTimeMillis()
        )
    }
}