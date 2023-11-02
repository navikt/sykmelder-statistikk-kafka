package no.nav.syfo.models.kafka

data class KafakMessageMetadata(val metadata: Metadata)

data class KafakMessageDataTest(val data: DataTest, val metadata: Metadata)

data class Metadata(val type: String)

data class DataTest(
    var PK: Int,
    var AARMND: String,
    var SYKM_HOVEDGRUPPE_KODE: String,
    var SYKM_UNDERGRUPPE_KODE: String,
    var SYKMELDER_SAMMENL_TYPE_KODE: String,
    var KJONN_KODE: String,
    var ALDER: Int,
    var ALDER_GRUPPE5_BESK: String,
    var BYDEL_NR: String,
    var KOMMUNE_NR: String,
    var FYLKE_NR: String,
    var ALDER_YRKESAKTIV_FLAGG: Int,
    var NAERING_INNTEKT_KATEGORI: String,
    var IKKE_ARBEIDSTAKER_FLAGG: Int,
    var RANGERING: Int,
    var PASIENT_ANTALL: Int,
    var ARBEID_ANTALL: Int,
)
