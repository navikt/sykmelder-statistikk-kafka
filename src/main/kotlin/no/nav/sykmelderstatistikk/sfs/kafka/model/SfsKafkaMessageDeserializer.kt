package no.nav.sykmelderstatistikk.sfs.kafka.model

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.apache.kafka.common.serialization.Deserializer

class SfsKafkaMessageDeserializer : Deserializer<SfsDataMessage<out DataType>> {
    private val objectMapper: ObjectMapper =
        jacksonObjectMapper().apply {
            registerModule(JavaTimeModule())
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true)
        }

    override fun deserialize(topic: String, value: ByteArray): SfsDataMessage<out DataType> {

        val jsonNode = objectMapper.readTree(value)
        val type = jsonNode["metadata"]["type"].asText()
        val message =
            when (type) {
                "AGG_SFS_VARIGHET_EGEN" ->
                    objectMapper.readValue<SfsDataMessage<AggSfsVarighetEgen>>(value)
                else -> SfsDataMessage(UnknownType(), Metadata(type))
            }

        return message
    }
}
