package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import java.io.File
import java.nio.file.Files.createDirectory
import java.nio.file.Paths

object FileController {
    val userDataFolder = "data/user"
    val recipeDataFolder = "data/recipe"

    fun uploadUserFile(file: FileItem, uniqueName: String, user: Long) {
        runCatching {
            createDirectory(Paths.get("$userDataFolder/$user"))
        }
        File("$userDataFolder/$user/$uniqueName").writeBytes(file.streamProvider().readBytes())
    }

    fun uploadRecipeImage(file: FileItem, recipe: Long, uniqueName: String) {
        runCatching {
            createDirectory(Paths.get("$recipeDataFolder/$recipe"))
        }
        File("$recipeDataFolder/$recipe/$uniqueName").writeBytes(file.streamProvider().readBytes())
    }

    fun getUserFile(uniqueName: String, user: Long): File = File("$userDataFolder/$user/$uniqueName")

    fun getRecipeImage(uniqueName: String, recipe: Long): File = File("$recipeDataFolder/$recipe/$uniqueName")
}