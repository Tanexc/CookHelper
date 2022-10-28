package ru.tanec.cookhelper.enterprise.use_case.recipeApi

import io.ktor.http.*
import ru.tanec.cookhelper.core.utils.FileController.getRecipeImage
import java.io.File


object GetRecipeImageUseCase {
    operator fun invoke(
        parameters: Parameters
    ): File {
        return getRecipeImage(parameters["path"] ?: "notFound.png") ?: File("/data/notFound.png")
    }
}

