package no.nav.sykmelderstatistikk.sfs.kafka.model

data class AggSfsVarighetEgen(
    val KOMPOSIT_KEY: String,
    val PK: Int,
    val AARMND: String,
    val SYKM_BYDEL_NAVN: String,
    val SYKM_KOMMUNE_NAVN: String,
    val SYKM_FYLKE_NAVN: String,
    val SYKM_FODSEL_NR: String,
    val SYKM_HOVEDGRUPPE_KODE: String,
    val SYKM_UNDERGRUPPE_KODE: String,
    val SYKMELDER_SAMMENL_TYPE_KODE: String,
    val PASIENT_KJONN_KODE: String,
    val PASIENT_ALDER_GRUPPE7_BESK: String,
    val HOVEDGRUPPE_SMP_BESK: String,
    val UNDERGRUPPE_SMP_BESK: String,
    val VARIGHET_GRUPPE9_BESK: String,
    val NAERING_GRUPPE6_BESK_LANG: String?,
    val ANTALL_SYKMELDINGER: Int,
    val GRADERT_FLAGG: Int,
    val ANTALL_DAGER: Int
) : DataType()
