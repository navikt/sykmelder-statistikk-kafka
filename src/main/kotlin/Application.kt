package no.nav.syfo

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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nav.syfo.kafka.aiven.KafkaUtils
import no.nav.syfo.kafka.toConsumerConfig
import no.nav.syfo.plugins.configureRouting
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val logger: Logger = LoggerFactory.getLogger("no.nav.syfo.sykmelder.statistikk.kafka")
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
    configureRouting(applicationState, environmentVariables.naisClusterName)

    val kafkaConsumer =
        KafkaConsumer<String, String>(
            KafkaUtils.getAivenKafkaConfig("kafka-canary-consumer")
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
                kafkaConsumer.subscribe(listOf(EnvironmentVariables().testTopic))
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
            val dataTest: DataTest = objectMapper.readValue(consumerRecord.value())
            handleMessage(dataTest)
        }
    }
}

fun handleMessage(
    dataTest : DataTest
) {
    logger.info("message from kafka: $dataTest")
}

data class DataTest(
    var pk : Int,
    var aarmnd : String,
    var sykm_houvedgruppe_kode : String,
    var sykm_undergruppe_kode : String,
    var sykmelder_sammenl_type_kode : String,
    var kjonn_kode : String,
    var alder : Int,
    var alder_gruppe5_besk : String,
    var bydel_nr : String,
    var kommune_nr : String,
    var fylke_nr : String,
    var alder_yrkesaktiv_flagg : Int,
    var naering_inntekt_kategori : String,
    var ikke_artbeidstaker_flagg : Int,
    var rangering : Int,
    var pasient_antall : Int,
    var arbeid_antall : Int,
)

data class ApplicationState(
    var alive: Boolean = true,
    var ready: Boolean = true,
)
