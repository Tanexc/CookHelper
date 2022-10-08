package ru.tanec.cookhelper.presentation.features.api.recipeApi.routing

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.enterprise.model.entity.User
import ru.tanec.cookhelper.enterprise.model.receive.userApi.RegistrationData
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.RecipeRepository
import ru.tanec.cookhelper.presentation.features.api.userApi.use_case.RegistrationUseCase


fun recipeApiRoutes(
    route: Routing,
    repository: RecipeRepository
) {

    route.post("/api/recipe/post/create") {

    }

    route.get("/api/recipe/get") {

    }
}
