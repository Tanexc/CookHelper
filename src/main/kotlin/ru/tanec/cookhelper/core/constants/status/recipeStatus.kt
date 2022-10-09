package ru.tanec.cookhelper.core.constants.status

object RecipeStatus {
    const val SUCCESS: Int = 200
    const val EXCEPTION: Int = 299
    const val RECIPE_NOT_FOUND: Int = 201
    const val WRONG_DATA: Int = 202
    const val PARAMETER_MISSED: Int = 204
    const val RECIPE_DELETED: Int = 205
    const val PERMISSION_DENIED: Int = 206
}