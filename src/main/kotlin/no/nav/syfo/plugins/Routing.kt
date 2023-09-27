package no.nav.syfo.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.syfo.ApplicationState
import no.nav.syfo.plugins.nais.isalive.naisIsAliveRoute
import no.nav.syfo.plugins.nais.isready.naisIsReadyRoute


fun Application.configureRouting(applicationState: ApplicationState) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
    routing {
        naisIsAliveRoute(applicationState)
        naisIsReadyRoute(applicationState)
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
    }
}
