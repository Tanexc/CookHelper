package ru.tanec.cookhelper.presentation.features.api.recipeApi.routing

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.core.utils.FileController.getRecipeImage
import ru.tanec.cookhelper.enterprise.model.entity.Recipe
import ru.tanec.cookhelper.enterprise.model.entity.User
import ru.tanec.cookhelper.enterprise.model.receive.recipeApi.RecipeData
import ru.tanec.cookhelper.enterprise.model.receive.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case.GetRecipeImageUseCase
import ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case.GetRecipeUseCase
import ru.tanec.cookhelper.presentation.features.api.recipeApi.use_case.RecipeCreateUseCase
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.RegistrationUseCase
import java.io.File


fun recipeApiRoutes(
    route: Routing,
    repository: RecipeRepository
) {

    route.post("/api/recipe/post/create") {

        call.respond(RecipeCreateUseCase(repository, call.receiveMultipart().readAllParts()))

    }

    route.get("/api/recipe/get") {

        call.respond(GetRecipeUseCase(repository, call.request.queryParameters))

    }

    route.get("/api/recipe/get/image") {
        try {
            call.respondFile(GetRecipeImageUseCase(call.request.queryParameters))
        } catch (_:Exception) {
            call.respondFile(GetRecipeImageUseCase(Parameters.Empty))
        }
    }

}
