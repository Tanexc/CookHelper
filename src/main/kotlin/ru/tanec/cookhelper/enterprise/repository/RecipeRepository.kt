package ru.tanec.cookhelper.enterprise.repository

import ru.tanec.cookhelper.core.db.dao.recipeDao.RecipeDao

interface RecipeRepository {
    val dao: RecipeDao
}