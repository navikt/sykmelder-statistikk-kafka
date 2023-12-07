package no.nav.sykmelderstatistikk

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import no.nav.sykmelderstatistikk.database.ExposedDatabase
import no.nav.sykmelderstatistikk.plugins.configureRouting
import no.nav.sykmelderstatistikk.routes.model.ApplicationState
import no.nav.sykmelderstatistikk.sfs.kafka.SfsDataConsumer
import no.nav.sykmelderstatistikk.unleash.createUnleashStateHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("no.nav.sykmelderstatistikk.kafka")
val securelogger: Logger = LoggerFactory.getLogger("securelog")

val objectMapper: ObjectMapper =
    ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

fun main() {
    val embeddedServer =
        embeddedServer(
            Netty,
            port = EnvironmentVariables().applicationPort,
            module = Application::module,
        )
    Runtime.getRuntime()
        .addShutdownHook(
            Thread {
                logger.info("Shutting down application from shutdown hook")
                embeddedServer.stop(TimeUnit.SECONDS.toMillis(10), TimeUnit.SECONDS.toMillis(10))
            },
        )
    embeddedServer.start(true)
}

fun Application.module() {
    val environmentVariables = EnvironmentVariables()
    val applicationState = ApplicationState()
    ExposedDatabase(environmentVariables)
    configureRouting(applicationState, environmentVariables.naisClusterName)
    val sharedScope = CoroutineScope(Dispatchers.IO)
    val sfsDataConsumer =
        SfsDataConsumer(
            environmentVariables = environmentVariables,
            scope = sharedScope,
        )
    createUnleashStateHandler(
        scope = sharedScope,
        toggle = SFS_DATA_CONSUMER_TOGGLE,
        onToggledOn = {
            logger.info("$SFS_DATA_CONSUMER_TOGGLE has been toggled on")
            sfsDataConsumer.start()
        },
        onToggledOff = {
            logger.warn("$SFS_DATA_CONSUMER_TOGGLE is toggled off, unsubscribing")
            sfsDataConsumer.stop()
        },
    )

    Runtime.getRuntime()
        .addShutdownHook(
            Thread {
                logger.info("Shutting down coroutines from shutdown hook")
                sharedScope.cancel()
            },
        )
}

const val SFS_DATA_CONSUMER_TOGGLE = "SFS_DATA_CONSUMER"
