package ru.tanec.cookhelper.core


sealed class State<T>(
    val data: T? = null,
    val message: String,
    val status: Int
) {

    class Success<T>(data: T? = null, message: String = "success", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Error<T>(data: T? = null, message: String = "error", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Interrupted<T>(data: T? = null, message: String = "interrupted", status: Int) :
        State<T>(data = data, message = message, status = status)

    class Processing<T>(data: T? = null, message: String = "processing", status: Int = 0) :
        State<T>(data = data, message = message, status = status)


    class Expired<T>(data: T? = null, message: String = "expired", status: Int) :
        State<T>(data = data, status = status, message = message)

}