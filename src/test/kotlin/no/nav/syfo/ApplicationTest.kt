package no.nav.syfo

import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.routing.routing
import io.ktor.server.testing.TestApplicationEngine
import io.ktor.server.testing.handleRequest
import no.nav.syfo.no.nav.syfo.plugins.nais.isalive.naisIsAliveRoute
import no.nav.syfo.no.nav.syfo.plugins.nais.isready.naisIsReadyRoute
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ApplicationTest {

    @Test
    internal fun `Returns ok on is_alive`() {
        with(TestApplicationEngine()) {
            start()
            val applicationState = ApplicationState()
            applicationState.ready = true
            applicationState.alive = true
            application.routing { naisIsAliveRoute(applicationState) }

            with(handleRequest(HttpMethod.Get, "/internal/is_alive")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("I'm alive! :)", response.content)
            }
        }
    }

    @Test
    internal fun `Returns ok in is_ready`() {
        with(TestApplicationEngine()) {
            start()
            val applicationState = ApplicationState()
            applicationState.ready = true
            applicationState.alive = true
            application.routing { naisIsReadyRoute(applicationState) }

            with(handleRequest(HttpMethod.Get, "/internal/is_ready")) {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("I'm ready! :)", response.content)
            }
        }
    }

    @Test
    internal fun `Returns internal server error when liveness check fails`() {
        with(TestApplicationEngine()) {
            start()
            val applicationState = ApplicationState()
            applicationState.ready = false
            applicationState.alive = false
            application.routing {
                naisIsReadyRoute(applicationState)
                naisIsAliveRoute(applicationState)
            }

            with(handleRequest(HttpMethod.Get, "/internal/is_alive")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("I'm dead x_x", response.content)
            }
        }
    }

    @Test
    internal fun `Returns internal server error when readyness check fails`() {
        with(TestApplicationEngine()) {
            start()
            val applicationState = ApplicationState()
            applicationState.ready = false
            applicationState.alive = false
            application.routing { naisIsReadyRoute(applicationState) }
            with(handleRequest(HttpMethod.Get, "/internal/is_ready")) {
                assertEquals(HttpStatusCode.InternalServerError, response.status())
                assertEquals("Please wait! I'm not ready :(", response.content)
            }
        }
    }
}
