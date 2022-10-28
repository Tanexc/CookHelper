package ru.tanec.cookhelper.core.utils

import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.util.date.*
import ru.tanec.cookhelper.core.constants.*
import ru.tanec.cookhelper.core.utils.HashTool.uniqueString
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import java.io.File
import java.nio.file.Files.createDirectory
import java.nio.file.Paths
import kotlin.random.Random

object FileController {
    fun uploadFile(folder: String, file: FileItem): FileData {
        val uniqueName = uniqueString(getTimeMillis().toString().reversed())
        val type = file.originalFileName?.split(".")?.get(1)?: ""

        runCatching {
            createDirectory(Paths.get(folder))
        }

        File("$folder/$uniqueName.$type").writeBytes(file.streamProvider().readBytes())
        return FileData(uniqueName, "$apiDomen/$folder/$uniqueName.$type")
    }

    fun String.toFileData(folder: String): FileData = FileData(
        id=this,
        link="$apiDomen/$folder/$this"
    )

}