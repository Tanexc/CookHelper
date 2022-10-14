package ru.tanec.cookhelper.core.di

import io.ktor.server.websocket.*
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.tanec.cookhelper.core.db.repository.*
import ru.tanec.cookhelper.enterprise.repository.*
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.repository.ChatConnectionRepository

val socketModule = module {
    singleOf(::ChatConnectionController)
    singleOf(::ChatConnectionRepository) { bind<ConnectionRepository<Long>>() }
    single { mutableMapOf<Long, MutableList<DefaultWebSocketServerSession>>() }
}

val apiRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl() }
    single<PostRepository> { PostRepositoryImpl() }

    single<CategoryRepository> { CategoryRepositoryImpl() }
    single<IngredientRepository> { IngredientRepositoryImpl() }
}