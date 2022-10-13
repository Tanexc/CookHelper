package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.util.date.*
import java.io.File
import java.nio.file.Files.createDirectory
import java.nio.file.Paths

object FileController {
    const val userDataFolder = "data/user"
    const val recipeDataFolder = "data/recipe"

    fun uploadUserFile(file: FileItem, user: Long): String {
        runCatching {
            createDirectory(Paths.get("$userDataFolder/$user"))
        }
        val uniqueName =
            "${(getTimeMillis().toString().reversed().toInt() / 35333442.9).toInt()}${file.originalFileName}"
        File("$userDataFolder/$user/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return uniqueName
    }

    fun uploadRecipeImage(file: FileItem, category: Long): String {
        val uniqueName = "${(getTimeMillis().toString().reversed().toInt() / 35333442.9).toInt()}_${category}_${file.originalFileName}"
        runCatching {
            createDirectory(Paths.get("$recipeDataFolder/$category"))
        }
        File("$recipeDataFolder/$category/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return "$category/$uniqueName"
    }

    fun getUserFile(uniqueName: String, user: Long): File = File("$userDataFolder/$user/$uniqueName")

    fun getRecipeImage(path: String): File? {
        runCatching {
            return File("$recipeDataFolder/$path")
        }
        return null
    }
}