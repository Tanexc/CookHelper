package ru.tanec.cookhelper.database.dao.fileDao

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.tanec.cookhelper.database.factory.DatabaseFactory.dbQuery
import ru.tanec.cookhelper.database.model.FileDatas
import ru.tanec.cookhelper.enterprise.model.entity.attachment.FileData

class FileDataDaoImpl: FIleDataDao {

    private fun convertToFileData(row: ResultRow): FileData = FileData(
        id=row[FileDatas.id],
        name=row[FileDatas.name],
        type=row[FileDatas.type],
        link=row[FileDatas.link]
    )

    private fun ResultRow.asFileData(): FileData = FileData(
        id=this[FileDatas.id],
        name=this[FileDatas.name],
        type=this[FileDatas.type],
        link=this[FileDatas.link]
    )

    override suspend fun getById(id: Long): FileData? = dbQuery {
        FileDatas
            .select{ FileDatas.id eq id}
            .map(::convertToFileData)
            .singleOrNull()
    }

    override suspend fun getByName(name: String): FileData? = dbQuery {
        FileDatas
            .select{ FileDatas.name eq name}
            .map(::convertToFileData)
            .singleOrNull()
    }

    override suspend fun getByListId(listId: List<Long>): List<FileData> = listId.mapNotNull { getById(it) }


    override suspend fun insert(data: FileData): FileData = dbQuery {
        FileDatas
            .insert{
                it[name] = data.name
                it[link] = data.link
                it[type] = data.type
            }.resultedValues?.first()?.asFileData()?: data

    }

}