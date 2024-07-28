package example.com.di

import com.mongodb.client.MongoClients
import example.com.data.PendingDataSource
import example.com.room.RoomController
import org.koin.dsl.module

val MainModule = module {

    // Fetch MongoDB URI and database name from the configuration
    val mongoUri = "mongodb://localhost:27017"
    val databaseName = "Hello_Chat"

    single {
        MongoClients.create(mongoUri).getDatabase(databaseName)
    }
    single {
        PendingDataSource(get())
    }

    // Define RoomController instance
    single {
        RoomController(get())
    }


}