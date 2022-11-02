package ru.tanec.cookhelper.presentation.features.api.ingredientApi

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository
import ru.tanec.cookhelper.enterprise.use_case.ingredientApi.IngGetAllUseCase
import ru.tanec.cookhelper.enterprise.use_case.ingredientApi.IngGetByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.ingredientApi.IngGetPartUseCase

fun Routing.ingredientApiRoutes() {

    val ingredientRepository: IngredientRepository by inject()

    get("api/ingredient/get/all/") {
        call.respond(IngGetAllUseCase(ingredientRepository))
    }

    get("api/ingredient/get/part/") {
        call.respond(IngGetPartUseCase(ingredientRepository, call.request.queryParameters))
    }

    get("api/ingredient/get/id/") {
        call.respond(IngGetByIdUseCase(ingredientRepository, call.request.queryParameters))
    }
}