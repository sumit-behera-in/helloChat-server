package example.com.data

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Message(
    @BsonId
    val _id: String = ObjectId().toString(),
    val text: String,
    val sender: String,
    val receiver: String,
    val timeStamp: Long,
    val messageStatus: String
)
