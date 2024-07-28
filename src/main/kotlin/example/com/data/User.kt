package example.com.data

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val name: String,
    val sessionId: String,
)