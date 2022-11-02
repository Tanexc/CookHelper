package ru.tanec.cookhelper.database.utils

import io.ktor.http.content.*
import io.ktor.http.content.PartData.*
import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import ru.tanec.cookhelper.core.constants.*
import ru.tanec.cookhelper.core.utils.HashTool.uniqueString
import ru.tanec.cookhelper.database.dao.fileDao.FileDataDaoImpl
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData
import java.io.File
import java.nio.file.Files.createDirectory
import java.nio.file.Paths

object FileController {



    private val dao = FileDataDaoImpl()

    suspend fun uploadFile(folder: String, file: FileItem, type: String): FileData {
        val uniqueName = "${uniqueString(getTimeMillis().toString().reversed())}.${EXTENTIONS[type]}"

        runCatching { createDirectory(Paths.get(folder)) }

        if (EXTENTIONS[type] == null) return FileData(-1, uniqueName, "$apiDomen/$folder/$uniqueName.${EXTENTIONS[type]}", type)

        File("$folder/$uniqueName").writeBytes(file.streamProvider().readBytes())
        return dao.insert(FileData(-1, uniqueName, "$apiDomen/$folder/$uniqueName.${EXTENTIONS[type]}", type))
    }

    fun toFileData(name: String): FileData? {
        return runBlocking {
            dao.getByName(name)
        }
    }
}


