package example.com.room

import example.com.data.Message
import example.com.data.PendingDataSource
import example.com.data.User
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val messageDataSource: PendingDataSource,
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun join(member: Member) {
        if (members.containsKey(member.userName)) {
            throw MemberAlreadyExistException()
        }
        members[member.userName] = Member(member.userName, member.sessionId, member.sockets)
    }

    fun getAllMembers(): List<User> {
        return members.keys().toList().map {
            User(
                name = it,
                sessionId = members[it]?.sessionId.toString()
            )
        }
    }

    suspend fun sendMessage(message: Message) {
        if (message.receiver in members.keys) {
            val content = Json.encodeToString(message)
            members[message.receiver]?.sockets?.send(Frame.Text(content))
        } else {
            messageDataSource.addPendingMessage(message)
        }
    }

    suspend fun tryDisconnect(userName: String) {
        members[userName]?.sockets?.close()
        if (members.containsKey(userName)) {
            members.remove(userName)
        }
    }

    suspend fun sendPendingMessages(userName: String) {
        return messageDataSource.sendPendingMessages(userName, members[userName]?.sockets)
    }


}