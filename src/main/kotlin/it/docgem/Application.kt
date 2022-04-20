package it.docgem


import io.ktor.server.application.*
import it.docgem.dao.DatabaseFactory
import it.docgem.plugins.*
import registerCountryTRoutes

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()
    configureRouting()
    registerCountryTRoutes()
    configureSerialization()
}
