package ru.tanec.cookhelper.presentation.features.api.feedApi.use_case

import io.ktor.http.content.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.status.RecipeStatus
import ru.tanec.cookhelper.core.utils.FileController.uploadPostFile
import ru.tanec.cookhelper.enterprise.model.entity.post.Post
import ru.tanec.cookhelper.enterprise.model.receive.postApi.PostData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.PostRepository

object PostCreateUseCase {
    suspend operator fun invoke(
        repository: PostRepository,
        parameters: List<PartData>
    ): ApiResponse<Post> {

        val state = when (val post = fromMultipart(parameters)?.asDomain()) {
            null -> {
                State.Error(status = RecipeStatus.PARAMETER_MISSED)
            }

            else -> {
                repository.insert(
                    post
                ).last()
            }
        }

        return ApiResponse(state.status, state.message, state.data)
    }


    private fun fromMultipart(partList: List<PartData>): PostData? {
        var authorId: Long? = null
        var text: String? = null
        var attachment: List<PartData.FileItem> = listOf()
        var images: List<PartData.FileItem> = listOf()

        partList.forEach { pt ->
            when (pt) {
                is PartData.FormItem -> {
                    when (pt.name) {
                        "authorId" -> {
                            authorId = pt.value.toLong()
                        }

                        "text" -> {
                            text = pt.value
                        }

                        else -> {}
                    }
                }

                is PartData.FileItem -> {

                    images = images + listOf(pt)
                }

                else -> {}
            }

        }


        return if ((authorId != null) and (text != null)) PostData(
            authorId,
            text,
            attachment,
            images
        ) else null


    }

    private fun PostData.asDomain(): Post {

        return Post(
            authorId = this.authorId ?: 0,
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