package ru.tanec.cookhelper.core

import ru.tanec.cookhelper.core.constants.status.EXCEPTION
import ru.tanec.cookhelper.core.constants.status.SUCCESS
import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse


sealed class State<T>(
    val data: T? = null,
    val addition: Any? = null,
    val message: String,
    val status: Int
) {

    fun asApiResponse(data: T? = null): ApiResponse<T?> = ApiResponse(this.status, this.message, data ?: this.data)

    fun asWebsocketResponse(): WebsocketResponse<T> = WebsocketResponse(this.status, this.data)

    class Success<T>(data: T? = null, addition: Any? = null,  message: String = "success", status: Int = SUCCESS) :
        State<T>(data = data, message = message, status = status, addition = addition)

    class Error<T>(data: T? = null, message: String = "error", status: Int = EXCEPTION) :
        State<T>(data = data, message = message, status = status)

    class Interrupted<T>(data: T? = null, message: String = "interrupted", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Processing<T>(data: T? = null, message: String = "processing", status: Int = 0) :
        State<T>(data = data, message = message, status = status)


    class Expired<T>(data: T? = null, message: String = "expired", status: Int) :
        State<T>(data = data, status = status, message = message)

}