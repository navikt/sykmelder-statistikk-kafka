package no.nav.sykmelderstatistikk.sfs.kafka

import com.fasterxml.jackson.core.JsonProcessingException
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
import no.nav.sykmelderstatistikk.securelogger
import no.nav.sykmelderstatistikk.sfs.SfsDataService
import no.nav.sykmelderstatistikk.sfs.kafka.model.AggSfsVarighetEgen
import no.nav.sykmelderstatistikk.sfs.kafka.model.DataType
import no.nav.sykmelderstatistikk.sfs.kafka.model.SfsDataMessage
import no.nav.sykmelderstatistikk.sfs.kafka.model.SfsKafkaMessageDeserializer
import no.nav.sykmelderstatistikk.sfs.kafka.model.UnknownType
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecords
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.slf4j.LoggerFactory
import toSykmeldingVarighet

private val consumer =
    KafkaConsumer<String, SfsDataMessage<DataType>>(
        KafkaUtils.getAivenKafkaConfig("dvh-consumer")
            .toConsumerConfig(
                "${EnvironmentVariables().applicationName}-consumer",
                valueDeserializer = SfsKafkaMessageDeserializer::class,
            )
            .also {
                it[ConsumerConfig.AUTO_OFFSET_RESET_CONFIG] = "earliest"
                it[ConsumerConfig.MAX_POLL_RECORDS_CONFIG] = 100
                it[ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG] = true
            },
    )

class SfsDataConsumer(
    private val kafkaConsumer: KafkaConsumer<String, SfsDataMessage<DataType>> = consumer,
    private val environmentVariables: EnvironmentVariables,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
    private val sfsDataService: SfsDataService,
) {

    private var job: Job? = null

    private val dataTypes: MutableMap<String, Int> = mutableMapOf()
    private val lastOffsets: MutableMap<Int, Long> = mutableMapOf()

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

    private suspend fun consume(coroutineScope: CoroutineScope) {
        kafkaConsumer.subscribe(listOf(environmentVariables.sfsDataTopic))
        coroutineScope.startLogging()
        while (coroutineScope.isActive) {
            val records = kafkaConsumer.poll(POLL_DURATION_SECONDS.seconds.toJavaDuration())
            try {
                handleSfsKafkaMessage(records)
            } catch (e: Exception) {
                log.error(
                        "Error running kafkaConsumer see secure log for details",
                )
                securelogger.error("Error from kafka-consumer", e)
                kafkaConsumer.unsubscribe()
                delay(10.seconds)
                kafkaConsumer.subscribe(listOf(environmentVariables.sfsDataTopic))
            }

            if (!records.isEmpty) {
                lastOffsets[records.last().partition()] = records.last().offset()
            }

        }
        kafkaConsumer.unsubscribe()
    }

    private fun handleSfsKafkaMessage(
        consumerRecord: ConsumerRecords<String, SfsDataMessage<DataType>>
    ) {
        try {
            val sfsMessages = consumerRecord.map { it.value() }.groupBy { it.data::class }

            sfsMessages.forEach {
                val type = it.key.simpleName ?: "no-name"
                dataTypes[type] = dataTypes.getOrDefault(type, 0) + it.value.size
                when (it.key) {
                    AggSfsVarighetEgen::class ->
                        sfsDataService.updateData(
                                it.value.map { aggSfsVarighetEgen ->
                                    toSykmeldingVarighet(aggSfsVarighetEgen.data as AggSfsVarighetEgen)
                                },
                        )

                    UnknownType::class ->
                        log.info("unknown types ${it.value.map { it.metadata.type }.distinct()}")
                }
            }
        } catch (ex: JsonProcessingException) {
            dataTypes["NOT_JSON"] = dataTypes.getOrDefault("NOT_JSON", 0) + 1
        }
    }

    private fun CoroutineScope.startLogging(): Job {
        return launch(Dispatchers.IO) {
            while (isActive) {
                delay(10.seconds)
                dataTypes.forEach { entry -> log.info("Key: ${entry.key}, Count: ${entry.value}") }
                log.info("offsets ${lastOffsets.map { (p, o) -> "P:$p O:$o" }}")
            }
        }
    }
}
