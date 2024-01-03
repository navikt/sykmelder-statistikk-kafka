package no.nav.sykmelderstatistikk.sfs.database

import java.time.LocalDate
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.date

object SfsSykmeldingTable : Table(name = "sfs_sykmelding") {
    val pk: Column<Int> = integer("pk")
    val ar: Column<Int> = integer("ar")
    val mnd: Column<Int> = integer("mnd")
    val bydel: Column<String?> = text("bydel").nullable()
    val kommune: Column<String> = text("kommune")
    val fylke: Column<String> = text("fylke")
    val sykmelderFnr: Column<String> = text("sykmelder_fnr")
    val hovedgruppeKode: Column<String> = text("hovedgruppe_kode")
    val undergruppeKode: Column<String> = text("undergruppe_kode")
    val sammensattKode: Column<String> = text("samensatt_kode")
    val pasientKjonn: Column<String> = text("pasient_kjonn")
    val pasientAlder: Column<Int> = integer("pasient_alder")
    val diagnoseHovedgruppe: Column<String> = text("diagnose_hovedgruppe")
    val diagnoseUndergruppe: Column<String> = text("diagnose_undergruppe")
    val naeringsgruppe: Column<String?> = text("naeringsgruppe").nullable()
    val inntektskategori: Column<String> = text("inntektskategori")
    val gradert: Column<Boolean> = bool("gradert")
    val antallDager: Column<Int> = integer("antall_dager")
    val sykefravarFom: Column<LocalDate> = date("sykefravar_fom")
    val fom: Column<LocalDate> = date("fom")
    val tom: Column<LocalDate> = date("tom")
    override val primaryKey = PrimaryKey(pk)
}
