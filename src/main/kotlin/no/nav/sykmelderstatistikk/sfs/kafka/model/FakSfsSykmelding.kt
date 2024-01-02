package no.nav.sykmelderstatistikk.sfs.kafka.model

data class FakSfsSykmelding(
    val PK: Int,
    val AARMND: String,
    val SYKM_FODSEL_NR: String,
    val NAERING_GRUPPE6_BESK_LANG: String,
    val INNTEKT_KATEGORI_NAVN: String,
    val PASIENT_KJONN_KODE: String,
    val PASIENT_ALDER: Int,
    val HOVEDGRUPPE_SMP_BESK: String,
    val UNDERGRUPPE_SMP_BESK: String,
    val SYKM_BYDEL_NAVN: String,
    val SYKM_KOMMUNE_NAVN: String,
    val SYKM_FYLKE_NAVN: String,
    val GRAD: Int,
    val SYKM_HOVEDGRUPPE_KODE: String,
    val SYKM_UNDERGRUPPE_KODE: String,
    val SYKMELDER_SAMMENL_TYPE_KODE: String,
    val GRADERT_FLAGG: Int,
    val VARIGHET_DAGER: Int,
    val SYKMELDING_ANTALL: Int,
    val GRADERT_ANTALL: Int,
    val SYKEFRAVAR_FRA_DATO: String,
    val SYKMELDT_FRA_DATO: String,
    val SYKMELDT_TIL_DATO: String,
    val SYKMELDING_GRAD: Int,
    ) :  DataType()




