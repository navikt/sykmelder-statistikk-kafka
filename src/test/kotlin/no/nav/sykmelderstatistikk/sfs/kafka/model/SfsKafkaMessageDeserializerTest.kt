package no.nav.sykmelderstatistikk.sfs.kafka.model

import org.amshove.kluent.shouldNotBeEqualTo
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import toSykmeldingVarighet

class SfsKafkaMessageDeserializerTest {

    @Test
    fun `test deserialization with AggSfsVarighetEgen type`() {
        val json =
            """
            {
              "metadata": {
                "type": "AGG_SFS_VARIGHET_EGEN"
              },
              "data": {
                "KOMPOSIT_KEY": "1_AGG_SFS_VARIGHET_EGEN",
                "PK": 1,
                "AARMND": "202202",
                "SYKM_BYDEL_NAVN": "n/a",
                "SYKM_KOMMUNE_NAVN": "Test",
                "SYKM_FYLKE_NAVN": "Test",
                "SYKM_FODSEL_NR": "1234567890",
                "SYKM_HOVEDGRUPPE_KODE": "KI",
                "SYKM_UNDERGRUPPE_KODE": "KI",
                "SYKMELDER_SAMMENL_TYPE_KODE": "KI",
                "PASIENT_KJONN_KODE": "M",
                "PASIENT_ALDER_GRUPPE7_BESK": "fra 35 - 49 år",
                "HOVEDGRUPPE_SMP_BESK": "Muskel og skjelett",
                "UNDERGRUPPE_SMP_BESK": "Nakke, skulder, arm",
                "VARIGHET_GRUPPE9_BESK": "1 til 4 uker",
                "NAERING_GRUPPE6_BESK_LANG": "Selvstendig næringsdrivende og arbeidsledige med dagpenger og frilansere o.a",
                "ANTALL_SYKMELDINGER": 1,
                "GRADERT_FLAGG": 0,
                "ANTALL_DAGER": 8
              }
            }
        """
                .trimIndent()
        val kafkaDeserializer = SfsKafkaMessageDeserializer()
        val message = kafkaDeserializer.deserialize("topic", json.toByteArray())

        val data = message.data as AggSfsVarighetEgen
        val mappedData = toSykmeldingVarighet(data)
        mappedData.mnd shouldNotBeEqualTo null
        assertEquals("1_AGG_SFS_VARIGHET_EGEN", data.KOMPOSIT_KEY)
    }
}
