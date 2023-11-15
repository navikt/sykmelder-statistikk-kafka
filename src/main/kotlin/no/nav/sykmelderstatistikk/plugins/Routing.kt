package no.nav.sykmelderstatistikk.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.sykmelderstatistikk.models.application.ApplicationState
import no.nav.sykmelderstatistikk.routes.nais.naisInternalRoutes

fun Application.configureRouting(applicationState: ApplicationState, naisClusterName: String) {
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            applicationState.alive = false
            applicationState.ready = false
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }

    routing {
        naisInternalRoutes(applicationState)

        if (naisClusterName == "dev-gcp" || naisClusterName == "localhost") {
            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        }
    }
}
