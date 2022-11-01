package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveReplyData
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController

fun topicWebsocketRoutes(
    route: Routing,
    controller: TopicConnectionController
) {

    route.webSocket("/websocket/topic/") {
        val flow = controller.connect(this, call.parameters)
        var user: User?
        flow.collect {
            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    try {
                        for (frame in incoming) {

                            val data: ForumReceiveReplyData = this.receiveDeserialized()
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
                    } finally {
                        controller.disconnect(this, it.data!!.id)
                        this.close()
                    }
                }
            }
        }
    }
}
