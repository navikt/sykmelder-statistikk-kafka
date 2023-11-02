package no.nav.syfo.models.kafka

import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import no.nav.syfo.objectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class KafkaMessageTest {

    @Test
    fun `Should map kafakMessage correctly from string to object`() {
        val kafkaMessageString = getFileAsString("src/test/resources/kafkamessage.json")
        val kafakMessage: KafakMessage = objectMapper.readValue<KafakMessage>(kafkaMessageString)
        val dataTest = objectMapper.readValue<DataTest>(kafakMessage.data)
        assertEquals(111111111, dataTest.PK)
    }
}

fun getFileAsString(filePath: String) =
    String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)