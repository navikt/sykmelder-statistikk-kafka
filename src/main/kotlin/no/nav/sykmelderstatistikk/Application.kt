package no.nav.sykmelderstatistikk

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*
import no.nav.syfo.kafka.aiven.KafkaUtils
import no.nav.syfo.kafka.toConsumerConfig
import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import no.nav.sykmelderstatistikk.database.ExposedDatabase
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest.handleSfsData
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsvarighetalle.handleSfsVarighetAlle
import no.nav.sykmelderstatistikk.models.KafkaMessageSfsDataTest
import no.nav.sykmelderstatistikk.models.KafkaMessageSfsVarighetAlle
import no.nav.sykmelderstatistikk.plugins.configureRouting
import no.nav.sykmelderstatistikk.routes.model.ApplicationState
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
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

@OptIn(DelicateCoroutinesApi::class)
fun Application.module() {
    val environmentVariables = EnvironmentVariables()
    val applicationState = ApplicationState()
    ExposedDatabase(environmentVariables)
    configureRouting(applicationState, environmentVariables.naisClusterName)

    val kafkaConsumer =
        KafkaConsumer<String, String>(
            KafkaUtils.getAivenKafkaConfig("dvh-consumer")
                .toConsumerConfig(
                    "${EnvironmentVariables().applicationName}-consumer",
                    valueDeserializer = StringDeserializer::class
                )
                .also { it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest" },
        )

    startConsumer(applicationState, kafkaConsumer)
}

@DelicateCoroutinesApi
fun startConsumer(
    applicationState: ApplicationState,
    kafkaConsumer: KafkaConsumer<String, String>
) {
    GlobalScope.launch(Dispatchers.IO) {
        while (applicationState.ready) {
            try {
                logger.info("Starting consuming topic")
                kafkaConsumer.subscribe(listOf(EnvironmentVariables().sfsDataTopic))
                start(applicationState, kafkaConsumer)
            } catch (ex: Exception) {
                logger.error(
                    "Error running kafka consumer, unsubscribing and waiting 10 seconds for retry",
                    ex
                )
                kafkaConsumer.unsubscribe()
                delay(10_000)
            }
        }
    }
}

private fun start(
    applicationState: ApplicationState,
    kafkaConsumer: KafkaConsumer<String, String>
) {
    while (applicationState.ready) {
        kafkaConsumer.poll(Duration.ofSeconds(10)).forEach { consumerRecord ->
            val metadata = objectMapper.readTree(consumerRecord.value())
            when (metadata.path("metadata").path("type").asText()) {
                "sfs_data_test" -> {
                    val kafakMessage: KafkaMessageSfsDataTest =
                        objectMapper.readValue(consumerRecord.value())
                    securelogger.info("diagnoseData from kafka is: $kafakMessage")
                    handleSfsData(kafakMessage)
                }
                "AGG_SFS_VARIGHET_ALLE" -> {
                    try {
                        val kafakMessage: KafkaMessageSfsVarighetAlle =
                            objectMapper.readValue(consumerRecord.value())
                        securelogger.info("diagnoseData from kafka is: $kafakMessage")
                        handleSfsVarighetAlle(kafakMessage)
                    } catch (e: Exception) {
                        logger.error(
                            "failing to read message with offset ${consumerRecord.offset()} and message ${consumerRecord.value()}"
                        )
                        throw e
                    }
                }
                else -> {
                    throw IllegalArgumentException(
                        "Unknown metadata type, kafka message is: ${objectMapper.writeValueAsString(metadata.path("metadata"))}"
                    )
                }
            }
        }
    }
}
