package example.com.data

import com.mongodb.client.MongoDatabase
import io.ktor.websocket.WebSocketSession

class PendingDataSource(
    private val db: MongoDatabase,
) {

    suspend fun addPendingMessage(message: Message) {
        // val collection = db.getCollection(message.receiver)

    }

    suspend fun sendPendingMessages(receiver: String, sockets: WebSocketSession?) {

    }

}
