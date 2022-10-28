package ru.tanec.cookhelper.core.utils

import de.nycode.bcrypt.hash
import de.nycode.bcrypt.verify
import kotlin.random.Random


object HashTool {
    fun getHash(password: String) = hash(password, 15)

    fun verifyHash(password: String, expected: ByteArray) = verify(password, expected)
    fun uniqueString(str: String): String {
        val hs = str.hashCode().toString()
        return "$hs-${Random.nextInt(9999).hashCode()}-$str"
    }
}