package example.com.room

import io.ktor.websocket.WebSocketSession

data class Member(
    val userName: String,
    val sessionId: String,
    val sockets: WebSocketSession,
)