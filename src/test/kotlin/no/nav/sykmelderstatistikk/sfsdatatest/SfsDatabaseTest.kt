package no.nav.sykmelderstatistikk.sfsdatatest

import io.mockk.every
import io.mockk.mockk
import no.nav.sykmelderstatistikk.config.EnvironmentVariables
import no.nav.sykmelderstatistikk.database.ExposedDatabase
import no.nav.sykmelderstatistikk.database.TestDb
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest.SfsDataTest
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest.handleSfsData
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsvarighetalle.SfsVarighetAlle
import no.nav.sykmelderstatistikk.database.upsertdatabase.sfsvarighetalle.handleSfsVarighetAlle
import no.nav.sykmelderstatistikk.models.*
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
    fun `Test save sfs test data`() {
        handleSfsData(
            KafkaMessageSfsDataTest(
                metadata = SfsMetadataTest(type = "sfs_data_test"),
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

    @Test
    fun `Test save sfs alle data`() {
        handleSfsVarighetAlle(
            KafkaMessageSfsVarighetAlle(
                metadata = SfsMetadata(type = "sfs_data_test"),
                data =
                    SfsVarighetAllePayload(
                        PK = 96,
                        AARMND = "todo",
                        SYKM_BYDEL_NAVN = "todo",
                        SYKM_KOMMUNE_NAVN = "todo",
                        SYKM_FYLKE_NAVN = "todo",
                        SYKM_HOVEDGRUPPE_KODE = "todo",
                        SYKM_UNDERGRUPPE_KODE = "todo",
                        SYKMELDER_SAMMENL_TYPE_KODE = "todo",
                        PASIENT_KJONN_KODE = "todo",
                        PASIENT_ALDER_GRUPPE7_BESK = "todo",
                        HOVEDGRUPPE_SMP_BESK = "todo",
                        UNDERGRUPPE_SMP_BESK = "todo",
                        VARIGHET_GRUPPE9_BESK = "todo",
                        NAERING_GRUPPE6_BESK_LANG = "todo",
                        ANTALL_SYKMELDINGER = 1,
                        GRADERT_FLAGG = 200,
                        ANTALL_DAGER = 23,
                    )
            )
        )

        val result = transaction { SfsVarighetAlle.select { SfsVarighetAlle.pk eq 96 }.single() }

        result[SfsVarighetAlle.antall_sykmeldinger] shouldBeEqualTo 1
        result[SfsVarighetAlle.gradert_flagg] shouldBeEqualTo 200
    }
}
