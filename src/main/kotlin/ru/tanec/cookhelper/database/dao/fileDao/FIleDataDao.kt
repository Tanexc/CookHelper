package ru.tanec.cookhelper.database.dao.fileDao

import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

interface FIleDataDao {
    suspend fun getById(id: Long): FileData?

    suspend fun getByListId(listId: List<Long>): List<FileData>

    suspend fun insert(data: FileData): FileData

    suspend fun getByName(name: String): FileData?
}