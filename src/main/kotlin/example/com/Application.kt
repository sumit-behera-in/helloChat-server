package example.com

import example.com.di.MainModule
import example.com.plugins.configureRouting
import example.com.plugins.configureSecurity
import example.com.plugins.configureSerialization
import example.com.plugins.configureSockets
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.websocket.WebSockets
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(WebSockets)
    
    install(Koin) {
        modules(MainModule)
    }

    configureSockets()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
