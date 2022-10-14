package ru.tanec.cookhelper.presentation.features.api.categoryApi.routing

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.tanec.cookhelper.enterprise.repository.CategoryRepository
import ru.tanec.cookhelper.presentation.features.api.categoryApi.use_case.*

fun categoryApiRoutes(
    route: Routing,
    repository: CategoryRepository
) {
    route.get("api/category/get/all") {
        call.respond(CatGetAllUseCase(repository))
    }

    route.get("api/category/get/part") {
        call.respond(CatGetPartUseCase(repository, call.request.queryParameters))
    }

    route.get("api/category/get/id") {
        call.respond(CatGetByIdUseCase(repository, call.request.queryParameters))
    }
}