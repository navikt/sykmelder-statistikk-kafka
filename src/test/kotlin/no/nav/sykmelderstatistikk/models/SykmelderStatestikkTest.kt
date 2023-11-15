package no.nav.sykmelderstatistikk.models

import no.nav.sykmelderstatistikk.models.kafka.DataTest
import no.nav.sykmelderstatistikk.toSykmelderStatestikk
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

internal class SykmelderStatestikkTest {

    @Test
    fun `Should map SykmelderStatestikk from DataTest`() {
        val dataTest =
            DataTest(
                PK = 349506982,
                AARMND = "202212",
                SYKM_HOVEDGRUPPE_KODE = "ALLM",
                SYKM_UNDERGRUPPE_KODE = "FAST",
                SYKMELDER_SAMMENL_TYPE_KODE = "ALLM",
                KJONN_KODE = "M",
                ALDER = 25,
                ALDER_GRUPPE5_BESK = "17-29 år",
                BYDEL_NR = "n/a",
                KOMMUNE_NR = "1120",
                FYLKE_NR = "11",
                ALDER_YRKESAKTIV_FLAGG = 1,
                NAERING_INNTEKT_KATEGORI = "Undervisning",
                IKKE_ARBEIDSTAKER_FLAGG = 0,
                RANGERING = 85,
                PASIENT_ANTALL = 1,
                ARBEID_ANTALL = 1
            )

        val sykmelderStatestikk = dataTest.toSykmelderStatestikk()

        Assertions.assertEquals(25, sykmelderStatestikk.alder)
        Assertions.assertEquals("2022", sykmelderStatestikk.aar)
        Assertions.assertEquals("12", sykmelderStatestikk.mnd)
    }
}
