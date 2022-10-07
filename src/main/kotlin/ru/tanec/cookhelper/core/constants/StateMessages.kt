package ru.tanec.cookhelper.core.constants


const val INVALID_TOKEN: String = "invalid token"
const val SUCCESS: String = "success"
const val SOME_ERROR: String = "something went wrong"
const val CLOSED: String = "connection closed"
const val MISSED: String = "parameter missed"

fun REQUIRED(parameter: String): String = "parameter $parameter requiered"