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
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import no.nav.syfo.kafka.aiven.KafkaUtils
import no.nav.syfo.kafka.toConsumerConfig
import no.nav.sykmelderstatistikk.database.ExposedDatabase
import no.nav.sykmelderstatistikk.models.application.ApplicationState
import no.nav.sykmelderstatistikk.models.application.EnvironmentVariables
import no.nav.sykmelderstatistikk.models.kafka.DataTest
import no.nav.sykmelderstatistikk.models.kafka.KafkaMessageDataTest
import no.nav.sykmelderstatistikk.models.sykmelderStatestikk.HouvedGruppe
import no.nav.sykmelderstatistikk.models.sykmelderStatestikk.Kjonn
import no.nav.sykmelderstatistikk.models.sykmelderStatestikk.SykmelderStatestikk
import no.nav.sykmelderstatistikk.models.sykmelderStatestikk.Type
import no.nav.sykmelderstatistikk.models.sykmelderStatestikk.UnderGruppe
import no.nav.sykmelderstatistikk.plugins.configureRouting
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
            val kafkaRawMessage = consumerRecord.value()
            val kafakMessage: KafkaMessageDataTest = objectMapper.readValue(consumerRecord.value())

            if (kafakMessage.metadata.type == "sfs_data_test") {

                securelogger.info("diagnoseData from kafka is: $kafakMessage")

                handleMessageKafkaDiagnoseData(kafakMessage)
            } else {
                throw IllegalArgumentException(
                    "Unknown metadata type, kafka message is: ${objectMapper.writeValueAsString(kafakMessage)}"
                )
            }
        }
    }
}

fun handleMessageKafkaDiagnoseData(kafkaDiagnoseData: KafkaMessageDataTest) {
    val sykmelderStatestikk = kafkaDiagnoseData.data.toSykmelderStatestikk()

    securelogger.info("sykmelderStatestikk is: $sykmelderStatestikk")
}

fun DataTest.toSykmelderStatestikk(): SykmelderStatestikk {
    return SykmelderStatestikk(
        primaryKey = PK,
        houvedGruppe = HouvedGruppe.valueOf(SYKM_HOVEDGRUPPE_KODE),
        underGruppe = UnderGruppe.valueOf(SYKM_UNDERGRUPPE_KODE),
        type = Type.valueOf(SYKMELDER_SAMMENL_TYPE_KODE),
        kjonn = Kjonn.valueOf(KJONN_KODE),
        alder = ALDER,
        aar = AARMND.slice(0..3),
        mnd = AARMND.slice(4..5),
        bydelNr = BYDEL_NR,
        fylkeNr = FYLKE_NR,
        kommuneNr = KOMMUNE_NR,
        aarYrkesAktiv = ALDER_YRKESAKTIV_FLAGG,
        naringInntektKategori = NAERING_INNTEKT_KATEGORI.trim(),
        ikkeArbeidstaker = IKKE_ARBEIDSTAKER_FLAGG == 0,
        rangering = RANGERING,
        antallPasienter = PASIENT_ANTALL,
        antallPasienterIArbeid = ARBEID_ANTALL
    )
}