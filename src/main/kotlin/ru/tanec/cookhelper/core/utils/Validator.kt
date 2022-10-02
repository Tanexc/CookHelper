package ru.tanec.cookhelper.core.utils

import ru.tanec.cookhelper.enterprise.utls.Validator.*
import ru.tanec.cookhelper.enterprise.utls.Validator

object ValidatorImpl: Validator {
    override fun isValidPassword(password: String): Validity {
        return if (password.length < 8) {
            Validity.Invalid("password must be 8 characters long")
        } else {
            Validity.Valid()
        }

    }

    override fun isValidLogin(login: String): Validity {
        return if (!(login.length in 5..15)) {
            Validity.Invalid("length of login must be in the range from 5 to 15 symbols")
        } else {
            Validity.Valid()
        }
    }
}