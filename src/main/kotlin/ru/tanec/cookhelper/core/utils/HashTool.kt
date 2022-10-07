package ru.tanec.cookhelper.core.utils

import de.nycode.bcrypt.hash
import de.nycode.bcrypt.verify


object HashTool {
    fun getHash(password: String) = hash(password, 8)

    fun verifyHash(password: String, expected: ByteArray) = verify(password, expected)
}