package ru.tanec.cookhelper.core

import ru.tanec.cookhelper.enterprise.model.response.ApiResponse
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse


sealed class State<T>(
    val data: T? = null,
    val addition: Any? = null,
    val message: String,
    val status: Int
) {

    fun asApiResponse(): ApiResponse<T> = ApiResponse(this.status, this.message, this.data)

    fun asWebsocketResponse(): WebsocketResponse<T> = WebsocketResponse(this.status, this.data)

    class Success<T>(data: T? = null, addition: Any? = null,  message: String = "success", status: Int) :
        State<T>(data = data, message = message, status = status, addition = addition)

    class Error<T>(data: T? = null, message: String = "error", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Interrupted<T>(data: T? = null, message: String = "interrupted", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Processing<T>(data: T? = null, message: String = "processing", status: Int = 0) :
        State<T>(data = data, message = message, status = status)


    class Expired<T>(data: T? = null, message: String = "expired", status: Int) :
        State<T>(data = data, status = status, message = message)

}