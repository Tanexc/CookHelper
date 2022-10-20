package ru.tanec.cookhelper.core.utils

import ru.tanec.cookhelper.enterprise.utls.Validator.*
import ru.tanec.cookhelper.enterprise.utls.Validator

object ValidatorImpl : Validator {
    override fun isValidPassword(password: String): Validity {
        return when (password.length < 8) {
            true -> Validity.Invalid("password must be 8 characters long")
            else -> Validity.Valid()
        }

    }

    override fun isValidLogin(login: String): Validity {
        return when (login.length !in 1..30) {
            true -> Validity.Invalid("length of login must be in the range from 1 to 30  symbols")
            else -> Validity.Valid()
        }
    }
}