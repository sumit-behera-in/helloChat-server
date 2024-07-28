package example.com.data

import kotlinx.serialization.Serializable
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message(
    @BsonId
    val _id: String = ObjectId().toString(),
    val text: String,
    var sender: String,
    val receiver: String,
    var timeStamp: Long,
    var messageStatus: String,
) {
    fun toDocument(): Document {
        return Document()
            .append("_id", _id)
            .append("text", text)
            .append("sender", sender)
            .append("receiver", receiver)
            .append("timeStamp", timeStamp)
            .append("messageStatus", messageStatus)
    }
}

fun Document.toMessage(): Message {
    return Message(
        _id = this.getString("_id"),
        text = this.getString("text"),
        sender = this.getString("sender"),
        receiver = this.getString("receiver"),
        timeStamp = this.getLong("timeStamp"),
        messageStatus = this.getString("messageStatus")
    )
}

@Serializable
data class Response(val text: String, val receiver: String) {
    fun toMessage(): Message {
        return Message(
            text = text,
            sender = "server",
            receiver = receiver,
            timeStamp = System.currentTimeMillis(),
            messageStatus = "send"
        )
    }
}