package ru.tanec.cookhelper.presentation.features.api.ingredientApi.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository
import ru.tanec.cookhelper.presentation.features.api.ingredientApi.use_case.*

fun ingredientApiRoutes(
    route: Routing,
    repository: IngredientRepository
) {
    route.get("api/ingredient/get/all/") {
        call.respond(IngGetAllUseCase(repository))
    }

    route.get("api/ingredient/get/part/") {
        call.respond(IngGetPartUseCase(repository, call.request.queryParameters))
    }

    route.get("api/ingredient/get/id/") {
        call.respond(IngGetByIdUseCase(repository, call.request.queryParameters))
    }
}