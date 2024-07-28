package example.com

import example.com.di.MainModule
import example.com.plugins.*
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {

    install(Koin) {
       modules(MainModule)
    }

    configureSockets()
    configureSerialization()
    configureSecurity()
    configureRouting()
}
