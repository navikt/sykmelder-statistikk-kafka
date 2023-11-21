package no.nav.sykmelderstatistikk.plugins

import io.ktor.http.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.sykmelderstatistikk.routes.model.ApplicationState
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class RoutingTest {
    @Test
    internal fun `Returns ok on swaggerUI when cluster dev-gcp`() {
        with(TestApplicationEngine()) {
            start()
            val naisClusterName = "dev-gcp"
            val applicationState = ApplicationState()
            applicationState.ready = true
            applicationState.alive = true
            application.routing {
                if (naisClusterName == "dev-gcp" || naisClusterName == "localhost") {
                    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
                }
            }

            with(handleRequest(HttpMethod.Get, "/swagger")) {
                Assertions.assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}
