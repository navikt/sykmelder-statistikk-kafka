package no.nav.sykmelderstatistikk.models.kafka

import com.fasterxml.jackson.module.kotlin.readValue
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths
import no.nav.sykmelderstatistikk.objectMapper
import no.nav.sykmelderstatistikk.sfsdatatest.KafkaMessageSfsDataTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test

internal class KafkaMessageTest {

    @Test
    fun `Should map kafakMessage correctly from string to object`() {
        val kafkaMessageString = getFileAsString("src/test/resources/kafkamessage.json")
        val kafakMessage: KafkaMessageSfsDataTest =
            objectMapper.readValue<KafkaMessageSfsDataTest>(kafkaMessageString)
        if (kafakMessage.metadata.type == "sfs_data_test") {
            assertEquals(349531485, kafakMessage.data.PK)
        } else {
            fail("unknown metadata type")
        }
    }
}

fun getFileAsString(filePath: String) =
    String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8)
