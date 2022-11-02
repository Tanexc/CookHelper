package ru.tanec.cookhelper.presentation.features.api.userApi

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.core.constants.MISSED
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository
import ru.tanec.cookhelper.enterprise.repository.api.RecipeRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.enterprise.use_case.userApi.*
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

fun Routing.userApiRoutes() {

    val userRepository: UserRepository by inject()
    val recipeRepository: RecipeRepository by inject()
    val ingredientRepository: IngredientRepository by inject()
    val userWebsocketConnectionController: UserWebsocketConnectionController by inject()

    post("/api/user/post/reg/") {
        try {
            val data = call.receiveMultipart().readAllParts()
            call.respond(RegistrationUseCase(userRepository, data))
        } catch (e: Exception) {
            call.respond(ApiResponse<User>(104, MISSED, null))
        }
    }

    post("/api/user/post/auth/") {
        val params = call.receiveMultipart().readAllParts()
        call.respond(LoginUseCase(userRepository, params))
    }

    get("/api/user/get/email-availability/") {
        val params = call.request.queryParameters
        call.respond(EmailAvailabilityUserUseCase(userRepository, params))
    }
    get("/api/user/get/nickname-availability/") {
        val params = call.request.queryParameters
        call.respond(NicknameAvailabilityUserUseCase(userRepository, params))
    }

    post("/api/user/post/fridge/insert/") {
        call.respond(InsertToFridgeUseCase(userRepository, ingredientRepository,  call.receiveMultipart().readAllParts()))
    }

    post("/api/user/post/fridge/remove/") {
        call.respond(RemoveFromFridgeUseCase(userRepository, ingredientRepository, call.receiveMultipart().readAllParts()))
    }

    get ("api/user/get/fridge/") {
        call.respond(GetFridgeUseCase(userRepository, ingredientRepository, call.request.queryParameters))
    }

    post("/api/user/post/avatar/") {
        call.respond(SetAvatarUseCase(userRepository, call.receiveMultipart().readAllParts()))
    }

    post("/api/user/post/image/") {
        call.respond(AddUserImageUseCase(userRepository, call.receiveMultipart().readAllParts()))
    }

    get("/api/user/get/fridge/recipe/") {
        call.respond(GetFridgeRecipeUseCase(recipeRepository, userRepository, call.request.queryParameters))
    }


}

