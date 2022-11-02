package ru.tanec.cookhelper.presentation.features.api.categoryApi

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.enterprise.repository.api.CategoryRepository
import ru.tanec.cookhelper.enterprise.use_case.categoryApi.CatGetAllUseCase
import ru.tanec.cookhelper.enterprise.use_case.categoryApi.CatGetByIdUseCase
import ru.tanec.cookhelper.enterprise.use_case.categoryApi.CatGetPartUseCase

fun Routing.categoryApiRoutes() {

    val categoryRepository: CategoryRepository by inject()

    get("api/category/get/all") {
        call.respond(CatGetAllUseCase(categoryRepository))
    }

    get("api/category/get/part") {
        call.respond(CatGetPartUseCase(categoryRepository, call.request.queryParameters))
    }

    get("api/category/get/id") {
        call.respond(CatGetByIdUseCase(categoryRepository, call.request.queryParameters))
    }
}