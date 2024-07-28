package example.com.data

import com.mongodb.client.MongoDatabase
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bson.Document

class PendingDataSource(
    private val db: MongoDatabase,
) {

    suspend fun addPendingMessage(message: Message) {
        coroutineScope {
            val messagesCollection = db.getCollection(message.receiver)
            try {
                messagesCollection.insertOne(message.toDocument())
            } catch (e: Exception) {
                // Handle exception
                println("Error adding pending message: $e")
            }
        }
    }

    suspend fun sendPendingMessages(receiver: String, sockets: WebSocketSession?) {
        sockets?.let {
            val messagesCollection = db.getCollection(receiver)
            val messages = messagesCollection.find().toList()
            messages.forEach { doc: Document ->
                try {
                    it.send(Frame.Text(Json.encodeToString(doc)))
                    messagesCollection.deleteOne(doc)
                } catch (e: Exception) {
                    // Handle exception
                    println("Error sending pending message: $e")
                }
            }
        }
    }

}
