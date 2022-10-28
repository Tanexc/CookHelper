package ru.tanec.cookhelper.presentation.features.api.recipeApi.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.core.constants.status.*
import ru.tanec.cookhelper.enterprise.model.entity.recipe.Recipe
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.CommentRepository
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.recipeApi.*


fun recipeApiRoutes(
    route: Routing,
    repository: RecipeRepository,
    userRepository: UserRepository,
    commentRepository: CommentRepository
) {

    route.post("/api/recipe/post/create/") {
        call.respond(RecipeCreateUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))

    }

    route.get("/api/recipe/get/") {

        call.respond(GetRecipeUseCase(repository, call.request.queryParameters))

    }

    route.get("/api/recipe/get/by-ingredients/") {
        //TODO
    }


    route.get("/api/recipe/post/like/") {
        try {
            call.respond(LikeRecipeUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

    route.get("/api/recipe/post/comment/") {
        try {
            call.respond(CommentRecipeUseCase(repository, userRepository, commentRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

    route.get("/api/recipe/post/repost/") {
        try {
            call.respond(RepostRecipeUseCase(repository, userRepository, call.receiveMultipart().readAllParts()))
        } catch (_:Exception) {
            call.respond(ApiResponse<Recipe>(EXCEPTION, "exception", null))
        }
    }

}
