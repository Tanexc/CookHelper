package ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.routing

import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import org.koin.ktor.ext.inject
import ru.tanec.cookhelper.core.State
import ru.tanec.cookhelper.enterprise.model.entity.user.User
import ru.tanec.cookhelper.enterprise.model.receive.topicWebsocket.ForumReceiveReplyData
import ru.tanec.cookhelper.presentation.features.websocket.topicsWebsocket.controller.TopicConnectionController

fun Routing.topicWebsocketRoutes() {

    val controller: TopicConnectionController by inject()

    webSocket("/websocket/topic/") {

        val flow = controller.connect(this, call.parameters)
        var user: User?

        flow.collect {
            if (it is State.Success) {
                user = it.addition as User?

                if (user != null) {

                    try {
                        var process = true
                        while (process) {
                            val data: ForumReceiveReplyData = this.receiveDeserialized()
                            when (val answer = controller.receiveMessage(data, user!!)) {
                                is State.Success -> {
                                    controller.sendMessage(
                                        answer.data!!,
                                        user!!,
                                        it.data!!.id
                                    )
                                }

                                else -> process = false
                            }
                        }
                    } finally {
                        controller.disconnect(this, it.data?.id)
                        this.close()
                        println(it.status)
                    }
                }
            }
        }
    }
}
