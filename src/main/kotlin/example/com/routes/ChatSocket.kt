package example.com.routes

import example.com.data.Response
import example.com.data.User
import example.com.room.Member
import example.com.room.MemberAlreadyExistException
import example.com.room.RoomController
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.chatSocket(roomController: RoomController) {
    webSocket("/chatSocket") { // websocketSession

        val sender = call.request.queryParameters["sender"] ?: "anonymous"

//        val session = call.sessions.get<ChatSession>()?.copy(
//            userName = sender
//        )
//
//        if (session == null) {
//            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session"))
//            return@webSocket
//        }


        try {
            val member = Member(
                userName = sender,
                sessionId = sender,
                sockets = this
            )
            roomController.join(member)
            roomController.sendPendingMessages(sender)

            val userData = Json.encodeToString(User(sender, "Connected"))
            outgoing.send(Frame.Text(userData))

            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    val text = frame.readText()
                    try {
                        val message = Json.decodeFromString<Response>(text).toMessage()
                        // Now you can use the `message` object
                        message.sender = sender
                        roomController.sendMessage(message)
                        outgoing.send(Frame.Text(Json.encodeToString(message)))
                    } catch (e: Exception) {
                        println("Error deserializing message: $e")
                    }

                }
            }

        } catch (e: MemberAlreadyExistException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
//            val userData = Json.encodeToString(User(session.userName, "disConnected"))
//            outgoing.send(Frame.Text(userData))
            roomController.tryDisconnect(sender)
        }
    }
}