package ru.tanec.cookhelper.domain.utls

interface Validator {
    fun isValidPassword(password: String): Validity
    fun isValidLogin(login: String): Validity

    sealed class Validity(
        val message: String?
        ) {
        class Valid(message: String? = "valid"): Validity(message=message)

        class Invalid(message: String? = "invalid"): Validity(message=message)

    }
}