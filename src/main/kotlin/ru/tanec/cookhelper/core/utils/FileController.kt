package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.util.date.*
import java.io.File
import java.nio.file.Files.createDirectory
import java.nio.file.Paths
import kotlin.random.Random

object FileController {
    const val userDataFolder = "data/user"
    const val recipeDataFolder = "data/recipe"
    const val feedDataFolder = "data/feed"
    const val attachmentDataFloder = "data/attachment"

    fun uploadUserFile(file: FileItem, user: Long): String {
        runCatching {
            createDirectory(Paths.get(userDataFolder))
        }
        val uniqueName =
            "${
                (("1" + getTimeMillis().toString().reversed()).toLong() / 35333442.9 + user).toInt()
            }${file.originalFileName}"
        File("$userDataFolder/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return uniqueName
    }

    fun uploadRecipeImage(file: FileItem, category: Long): String {
        val uniqueName = "${
            (("1" + getTimeMillis().toString().reversed()).toLong() / 38333410.9).toInt()
        }_${category}_${file.originalFileName}"
        runCatching {
            createDirectory(Paths.get(recipeDataFolder))
        }
        File("$recipeDataFolder/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return "$category/$uniqueName"
    }

    fun getUserFile(uniqueName: String, user: Long): File = File("$userDataFolder/$user/$uniqueName")

    fun getRecipeImage(path: String): File? {
        runCatching {
            return File(recipeDataFolder)
        }
        return null
    }

    fun uploadPostFile(file: FileItem, authorId: Long): String {
        val uniqueName = "${
            (("1" + getTimeMillis().toString().reversed()).toLong() / 35369442.9 + authorId).toInt()
        }_${authorId}_${file.originalFileName}"
        runCatching {
            createDirectory(Paths.get(feedDataFolder))
        }
        File("$feedDataFolder/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return uniqueName
    }

    fun getPostFile(path: String): File? {

        runCatching {
            return File("$feedDataFolder/$path")
        }
        return null
    }

    fun uploadAttachmentFile(file: FileItem): String {
        val uniqueName = "${file.hashCode()}" +
                "${Random.nextInt(2098)}" +
                "${getTimeMillis().toString().reversed().toLong() / 3536904.2}" +
                "${file.originalFileName}"
        runCatching {
            createDirectory(Paths.get(attachmentDataFloder))
        }
        File("$attachmentDataFloder/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return uniqueName
    }

    fun getAttachmentFile(path: String): File? {
        runCatching {
            return File("$attachmentDataFloder/$path")
        }
        return null
    }

}