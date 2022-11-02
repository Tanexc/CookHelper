package ru.tanec.cookhelper.presentation.features.api.recipeApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.recipeApi.*


fun Routing.recipeApiRoutes() {

    val userRepository: UserRepository by inject()
    val recipeRepository: RecipeRepository by inject()
    val commentRepository: CommentRepository by inject()

    post("/api/recipe/post/create/") {
        call.respond(RecipeCreateUseCase(recipeRepository, userRepository, call.receiveMultipart().readAllParts()))

    }

    get("/api/recipe/get/") {

        call.respond(GetRecipeUseCase(recipeRepository, call.request.queryParameters))

    }

    get("/api/recipe/get/by-ingredients/") {
        call.respond(GetRecipesByIngredientsUseCase(recipeRepository, userRepository, call.request.queryParameters))
    }


    get("/api/recipe/post/like/") {
        try {
            call.respond(LikeRecipeUseCase(recipeRepository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

    get("/api/recipe/post/comment/") {
        try {
            call.respond(CommentRecipeUseCase(recipeRepository, userRepository, commentRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

    get("/api/recipe/post/repost/") {
        try {
            call.respond(RepostRecipeUseCase(recipeRepository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

}
