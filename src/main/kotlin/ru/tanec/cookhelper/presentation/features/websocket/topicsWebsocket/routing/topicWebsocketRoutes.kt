package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.flow.forEach
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.forum.Topic
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveAnswerData
import ru.tanec.cookhelper.enterprise.model.response.WebsocketResponse
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController

fun topicWebsocketRoutes(
    route: Routing,
    controller: TopicConnectionController
) {

    route.webSocket("/websocket/api/topic/") {
        val flow = controller.connect(this, call.parameters)
        var user: User?
        flow.collect {
            this.sendSerialized(it.asWebsocketResponse())
            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    for (frame in incoming) {

                        val data: ForumReceiveAnswerData = this.receiveDeserialized()
                        when (val answer = controller.receiveMessage(data, user!!)) {
                            is State.Success -> {
                                controller.sendMessage(
                                    answer.data!!,
                                    user!!,
                                    it.data!!.id
                                )
                            }

                            else -> break
                        }
                    }
                }
            }
        }
    }
}
