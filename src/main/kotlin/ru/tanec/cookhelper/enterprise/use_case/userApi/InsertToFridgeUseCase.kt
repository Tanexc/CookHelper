package ru.tanec.cookhelper.enterprise.use_case.userApi

import io.ktor.http.content.*
import kotlinx.coroutines.flow.last
import ru.tanec.cookhelper.core.constants.status.PARAMETER_MISSED
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.core.constants.status.USER_TOKEN_INVALID
import ru.tanec.cookhelper.core.utils.JwtTool.isNotExpired
import ru.tanec.cookhelper.core.utils.checkUserToken
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.repository.api.IngredientRepository
import ru.tanec.cookhelper.enterprise.repository.api.UserRepository
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

object InsertToFridgeUseCase {
    suspend operator fun invoke(
        userRepository: UserRepository,
        ingredientRepository: IngredientRepository,
        userWebsocketConnectionController: UserWebsocketConnectionController,
        parameters: List<PartData>
    ): ApiResponse<User?> {

        var token: String? = null
        var fridge: List<Long>? = null
        var user: User? = null

        parameters.filterIsInstance<PartData.FormItem>().forEach { param ->
            when (param.name) {
                "token" -> {
                    if (param.value.isNotExpired()) {
                        token = param.value
                        user = checkUserToken(userRepository, token!!)
                    }
                }

                "fridge" -> {
                    fridge = param.value.split("*").mapNotNull { it.toLongOrNull() }
                        .filter { ingredientRepository.getById(it).last().data != null }
                }
            }
        }

        if (token == null || fridge == null) {
            return ApiResponse(
                status = PARAMETER_MISSED,
                data = null,
                message = "error"
            )
        }

        if (user != null) {
            user = user!!.copy(fridge = (user!!.fridge + fridge!!).toSet().toList())
            userWebsocketConnectionController.updateData(user!!, userRepository)
        }

        val userPrivate = user?.let { userRepository.edit(it).last().data?.privateInfo() }

        return userPrivate?.let {
            ApiResponse(
                status = SUCCESS,
                data = it,
                message = "success"
            )
        } ?: ApiResponse(
            status = USER_TOKEN_INVALID,
            data = null,
            message = "error"
        )
    }
}