package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.content.*
import io.ktor.util.date.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.core.constants.INVALID_TOKEN
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.comment.Comment
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object CommentRecipeUseCase {
    suspend operator fun invoke(
        recipeRepository: RecipeRepository,
        userRepository: UserRepository,
        commentRepository: CommentRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<Recipe> {

        var id: Long? = null
        var token: String? = null
        var text: String? = null

        parameters.forEach {
            when (it.name) {
                "id" -> {
                    if (it is PartData.FormItem) {
                        id = it.value.toLong()
                    }
                }

                "token" -> {
                    if (it is PartData.FormItem) {
                        token = it.value
                    }
                }

                "text" -> {
                    if (it is PartData.FormItem) {
                        text = it.value
                    }
                }
            }
        }

        if (id == null) return ApiResponse(PARAMETER_MISSED, MISSED, null)
        if (text == null) return ApiResponse(PARAMETER_MISSED, MISSED, null)
        if (token == null) return ApiResponse(PARAMETER_MISSED, INVALID_TOKEN, null)

        val data = recipeRepository.getById(id!!).last().data ?: return ApiResponse(
            RECIPE_NOT_FOUND,
            "recipe not found",
            null
        )
        val user = userRepository.getByToken(token!!).last().data ?: return ApiResponse(
            USER_NOT_FOUND,
            INVALID_TOKEN,
            null
        )

        userWebsocketConnectionController.updateData(user, userRepository)

        val commentState = commentRepository.insertComment(
            Comment(
                id = 0,
                authorId = user.id,
                text = text ?: "",
                timestamp = getTimeMillis()
            )
        ).last()

        return when (commentState) {
            is State.Success -> {
                val recipe = data.copy(comments = data.comments + listOf(commentState.data!!.id))
                val state = recipeRepository.editRecipe(recipe).last()

                ApiResponse(state.status, state.message, state.data)
            }

            else -> {
                ApiResponse(status = EXCEPTION, message = "comment not inserted", data = null)
            }
        }


    }

}