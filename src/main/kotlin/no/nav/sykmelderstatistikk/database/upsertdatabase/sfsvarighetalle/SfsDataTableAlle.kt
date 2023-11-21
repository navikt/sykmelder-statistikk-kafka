package no.nav.sykmelderstatistikk.database.upsertdatabase.sfsvarighetalle

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object SfsVarighetAlle : Table(name = "sfs_varighet_alle") {
    val pk: Column<Int> = integer("pk")
    val aarmnd: Column<String> = text("aarmnd")
    val sykm_bydel_navn: Column<String> = text("sykm_bydel_navn")
    val sykm_kommune_navn: Column<String> = text("sykm_kommune_navn")
    val sykm_fylke_navn: Column<String> = text("sykm_fylke_navn")
    val sykm_hovedgruppe_kode: Column<String> = text("sykm_hovedgruppe_kode")
    val sykm_undergruppe_kode: Column<String> = text("sykm_undergruppe_kode")
    val sykmelder_sammenl_type_kode: Column<String> = text("sykmelder_sammenl_type_kode")
    val pasient_kjonn_kode: Column<String> = text("pasient_kjonn_kode")
    val pasient_alder_gruppe7_besk: Column<String> = text("pasient_alder_gruppe7_besk")
    val hovedgruppe_smp_besk: Column<String> = text("hovedgruppe_smp_besk")
    val undergruppe_smp_besk: Column<String> = text("undergruppe_smp_besk")
    val varighet_gruppe9_besk: Column<String> = text("varighet_gruppe9_besk")
    val naering_gruppe6_besk_lang: Column<String> = text("naering_gruppe6_besk_lang")
    val antall_sykmeldinger: Column<Int> = integer("antall_sykmeldinger")
    val gradert_flagg: Column<Int> = integer("gradert_flagg")
    val antall_dager: Column<Int> = integer("antall_dager")

    override val primaryKey = PrimaryKey(pk)
}
