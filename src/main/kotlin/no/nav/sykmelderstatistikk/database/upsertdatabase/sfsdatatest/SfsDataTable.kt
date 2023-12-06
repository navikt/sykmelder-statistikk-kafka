package no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object SfsDataTest : Table(name = "sfs_data_test") {
    val pk: Column<Int> = integer("pk")
    val aarmnd: Column<String> = text("aarmnd")
    val sykmHovedgruppeKode: Column<String> = text("sykm_hovedgruppe_kode")
    val sykmUndergruppeKode: Column<String> = text("sykm_undergruppe_kode")
    val sykmelderSammenlTypeKode: Column<String> = text("sykmelder_sammenl_type_kode")
    val kjonnKode: Column<String> = text("kjonn_kode")
    val alder: Column<Int> = integer("alder")
    val alderGruppe5Besk: Column<String> = text("alder_gruppe5_besk")
    val bydelNr: Column<String> = text("bydel_nr")
    val kommuneNr: Column<String> = text("kommune_nr")
    val fylkeNr: Column<String> = text("fylke_nr")
    val alderYrkesaktivFlagg: Column<Int> = integer("alder_yrkesaktiv_flagg")
    val naeringInntektKategori: Column<String> = text("naering_inntekt_kategori")
    val ikkeArbeidstakerFlagg: Column<Int> = integer("ikke_arbeidstaker_flagg")
    val rangering: Column<Int> = integer("rangering")
    val pasientAntall: Column<Int> = integer("pasient_antall")
    val arbeidAntall: Column<Int> = integer("arbeid_antall")

    override val primaryKey = PrimaryKey(pk)
}
