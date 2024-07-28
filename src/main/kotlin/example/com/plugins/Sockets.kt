package example.com.plugins

import example.com.room.RoomController
import example.com.routes.chatSocket
import io.ktor.server.application.Application
import io.ktor.server.routing.routing
import org.koin.ktor.ext.inject

fun Application.configureSockets() {
    val roomController by inject<RoomController>()

    routing {
        chatSocket(roomController)
    }
}
