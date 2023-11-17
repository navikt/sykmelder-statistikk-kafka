package no.nav.sykmelderstatistikk.sfsdatatest

import io.mockk.every
import io.mockk.mockk
import no.nav.sykmelderstatistikk.database.ExposedDatabase
import no.nav.sykmelderstatistikk.database.TestDb
import no.nav.sykmelderstatistikk.models.application.EnvironmentVariables
import org.amshove.kluent.shouldBeEqualTo
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SfsDatabaseTest {

    @BeforeAll
    internal fun setup() {
        val environmentVariable =
            mockk<EnvironmentVariables>().apply {
                every { dbUsername } returns "username"
                every { dbPassword } returns "password"
                every { jdbcUrl() } returns TestDb.jdbcUrl
            }
        println("Doing the needful")
        ExposedDatabase(environmentVariable)
    }

    @Test
    fun `Test save sfs data`() {
        handleSfsData(
            KafkaMessageSfsDataTest(
                metadata = SfsMetadata(type = "sfs_data_test"),
                data =
                    SfsDataTestPayload(
                        PK = 69,
                        AARMND = "todo",
                        SYKM_HOVEDGRUPPE_KODE = "todo",
                        SYKM_UNDERGRUPPE_KODE = "todo",
                        SYKMELDER_SAMMENL_TYPE_KODE = "todo",
                        KJONN_KODE = "todo",
                        ALDER = 34,
                        ALDER_GRUPPE5_BESK = "todo",
                        BYDEL_NR = "todo",
                        KOMMUNE_NR = "todo",
                        FYLKE_NR = "todo",
                        ALDER_YRKESAKTIV_FLAGG = 1,
                        NAERING_INNTEKT_KATEGORI = "todo",
                        IKKE_ARBEIDSTAKER_FLAGG = 1,
                        RANGERING = 1,
                        PASIENT_ANTALL = 200,
                        ARBEID_ANTALL = 23,
                    )
            )
        )

        val result = transaction { SfsDataTest.select { SfsDataTest.pk eq 69 }.single() }

        result[SfsDataTest.alder] shouldBeEqualTo 34
        result[SfsDataTest.alderYrkesaktivFlagg] shouldBeEqualTo 1
    }
}
