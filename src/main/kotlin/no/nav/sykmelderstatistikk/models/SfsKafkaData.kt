package no.nav.sykmelderstatistikk.models

data class KafkaMessageSfsDataTest(val data: SfsDataTestPayload, val metadata: SfsMetadataTest)

data class SfsMetadataTest(val type: String)

data class SfsDataTestPayload(
    val PK: Int,
    val AARMND: String,
    val SYKM_HOVEDGRUPPE_KODE: String,
    val SYKM_UNDERGRUPPE_KODE: String,
    val SYKMELDER_SAMMENL_TYPE_KODE: String,
    val KJONN_KODE: String,
    val ALDER: Int,
    val ALDER_GRUPPE5_BESK: String,
    val BYDEL_NR: String,
    val KOMMUNE_NR: String,
    val FYLKE_NR: String,
    val ALDER_YRKESAKTIV_FLAGG: Int,
    val NAERING_INNTEKT_KATEGORI: String,
    val IKKE_ARBEIDSTAKER_FLAGG: Int,
    val RANGERING: Int,
    val PASIENT_ANTALL: Int,
    val ARBEID_ANTALL: Int,
)
