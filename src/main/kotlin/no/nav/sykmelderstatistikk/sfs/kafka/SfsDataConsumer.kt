package no.nav.sykmelderstatistikk.sfs.kafka

import com.fasterxml.jackson.module.kotlin.readValue
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import no.nav.syfo.kafka.aiven.KafkaUtils
import no.nav.syfo.kafka.toConsumerConfig
import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest.handleSfsData
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsvarighetalle.handleSfsVarighetAlle
import no.nav.sykmelderstatistikk.logger
import no.nav.sykmelderstatistikk.models.KafkaMessageSfsDataTest
import no.nav.sykmelderstatistikk.models.KafkaMessageSfsVarighetAlle
import no.nav.sykmelderstatistikk.objectMapper
import no.nav.sykmelderstatistikk.securelogger
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer
import org.slf4j.LoggerFactory

val consumer =
    KafkaConsumer<String, String>(
        KafkaUtils.getAivenKafkaConfig("dvh-consumer")
            .toConsumerConfig(
                "${EnvironmentVariables().applicationName}-consumer",
                valueDeserializer = StringDeserializer::class
            )
            .also { it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest" },
    )

class SfsDataConsumer(
    private val kafkaConsumer: KafkaConsumer<String, String> = consumer,
    private val environmentVariables: EnvironmentVariables,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) {

    private var job: Job? = null

    companion object {
        private const val POLL_DURATION_SECONDS = 1L
        private val log = LoggerFactory.getLogger(SfsDataConsumer::class.java)
    }

    suspend fun start() {
        if (job == null || job!!.isCompleted) {
            job = scope.launch(Dispatchers.IO) { consume(this) }
            log.info("Consumer started")
        } else (log.info("Consumer already running"))
    }

    fun stop() {
        if (job != null && job!!.isActive) {
            job?.cancel()
            job = null
            log.info("Consumer stopped")
        } else {
            log.info("Consumer already stopped")
        }
    }

    suspend fun consume(coroutineScope: CoroutineScope) {
        kafkaConsumer.subscribe(listOf(environmentVariables.sfsDataTopic))
        while (coroutineScope.isActive) {
            try {
                val records = kafkaConsumer.poll(POLL_DURATION_SECONDS.seconds.toJavaDuration())
                records.forEach { handleSfsKafkaMessage(it) }
            } catch (e: Exception) {
                log.error("Error running kafkaConsumer", e)
                kafkaConsumer.unsubscribe()
                delay(10.seconds)
                kafkaConsumer.subscribe(listOf(environmentVariables.sfsDataTopic))
            }
        }
        kafkaConsumer.unsubscribe()
    }

    private fun handleSfsKafkaMessage(consumerRecord: ConsumerRecord<String, String>) {
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
                    "Unknown metadata type, kafka message is: ${
                        objectMapper.writeValueAsString(
                            metadata.path("metadata")
                        )
                    }"
                )
            }
        }
    }
}
