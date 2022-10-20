package ru.tanec.cookhelper.database.dao.answerDao

import ru.tanec.cookhelper.enterprise.model.entity.forum.Answer

interface AnswerDao {
    suspend fun getById(id: Long): Answer?

    suspend fun getByListId(listId: List<Long>): List<Answer>

    suspend fun getByListId(listId: List<Long>, part: Int, div: Int): List<Answer>

    suspend fun insert(answer: Answer): Answer?
}