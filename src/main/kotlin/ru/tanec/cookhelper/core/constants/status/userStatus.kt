package ru.tanec.cookhelper.core.constants.status

object UserStatus {
    const val SUCCESS: Int = 100
    const val EXCEPTION: Int = 199
    const val USER_NOT_FOUND: Int = 101
    const val WRONG_CREDENTAILS: Int = 102
    const val NOT_VERIFIED: Int = 103
    const val PARAMETER_MISSED: Int = 104
    const val USER_DELETED: Int = 105
    const val PERMISSION_DENIED: Int = 106
    const val NICKNAME_REJECTED: Int = 107
    const val EMAIL_REJECTED: Int = 108
    const val PASSWORD_REJECTED: Int = 109
    const val TOKEN_EXPIRED: Int = 110
}