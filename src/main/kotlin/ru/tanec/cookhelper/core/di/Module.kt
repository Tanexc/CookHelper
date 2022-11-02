package ru.tanec.cookhelper.core.di


import org.koin.dsl.module
import ru.tanec.cookhelper.database.repository.*
import ru.tanec.cookhelper.enterprise.repository.api.*
import ru.tanec.cookhelper.presentation.features.websocket.chatWebsocket.controller.ChatConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController
import ru.tanec.cookhelper.presentation.features.websocket.userWebsocket.controller.UserWebsocketConnectionController

val socketModule = module {
    single {ChatConnectionController()}
    single {TopicConnectionController()}
    single { UserWebsocketConnectionController() }
}

val apiRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
    single<RecipeRepository> { RecipeRepositoryImpl() }
    single<PostRepository> { PostRepositoryImpl() }

    single<CategoryRepository> { CategoryRepositoryImpl() }
    single<IngredientRepository> { IngredientRepositoryImpl() }
    single<CommentRepository> { CommentRepositoryImpl() }

    single<TopicRepository> {TopicRepositoryImpl()}
    single<ChatRepository> {ChatRepositoryImpl()}
    single<MessageRepository> {MessageRepositoryImpl()}
}