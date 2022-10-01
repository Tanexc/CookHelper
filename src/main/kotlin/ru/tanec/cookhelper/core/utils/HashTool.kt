package ru.tanec.cookhelper.core.utils

import de.nycode.bcrypt.*

object HashTool {
    fun getHash(input: String): String = hash(input, 256).toString()


    fun checkHash(input: String, hash: String): Boolean =
        verify(input, hash.toByteArray())
}