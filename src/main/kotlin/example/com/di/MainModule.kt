package example.com.di

import com.mongodb.client.MongoClients
import com.typesafe.config.ConfigFactory
import example.com.data.PendingDataSource
import example.com.room.RoomController
import org.koin.dsl.module

val MainModule = module {
    val config = ConfigFactory.load()

    // Fetch MongoDB URI and database name from the configuration
    val mongoUri = "mongodb://localhost:27017"
    val databaseName = "Hello_Chat"

    single {
        MongoClients.create(mongoUri).getDatabase(databaseName)
        PendingDataSource(get())
        RoomController(get())
    }

}