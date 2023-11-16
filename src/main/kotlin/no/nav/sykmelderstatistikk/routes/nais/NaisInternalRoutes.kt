package no.nav.sykmelderstatistikk.routes.nais

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import no.nav.sykmelderstatistikk.models.application.ApplicationState

fun Routing.naisInternalRoutes(
    applicationState: ApplicationState,
) {
    get("/internal/is_alive") {
        if (applicationState.alive) {
            call.respondText("I'm alive! :)")
        } else {
            call.respondText("I'm dead x_x", status = HttpStatusCode.InternalServerError)
        }
    }

    get("/internal/is_ready") {
        if (applicationState.ready) {
            call.respondText("I'm ready! :)")
        } else {
            call.respondText(
                "Please wait! I'm not ready :(",
                status = HttpStatusCode.InternalServerError,
            )
        }
    }
}
