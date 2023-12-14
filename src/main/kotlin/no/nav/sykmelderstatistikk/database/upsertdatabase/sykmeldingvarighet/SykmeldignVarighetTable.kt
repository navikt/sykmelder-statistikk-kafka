package no.nav.sykmelderstatistikk.database.upsertdatabase.sykmeldingvarighet

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object SykmeldingVarighetTable : Table(name = "sykmelding_varighet") {
    val pk: Column<Int> = integer("pk")
    val ar: Column<Int> = integer("ar")
    val mnd: Column<Int> = integer("mnd")
    val bydel: Column<String?> = text("bydel").nullable()
    val kommune: Column<String> = text("kommune")
    val fylke: Column<String> = text("fylke")
    val hovedgruppeKode: Column<String> = text("hovedgruppe_kode")
    val undergruppeKode: Column<String> = text("undergruppe_kode")
    val sammensattKode: Column<String> = text("samensatt_kode")
    val pasientKjonn: Column<String> = text("pasient_kjonn")
    val pasientAldersgruppe: Column<String> = text("pasient_aldersgruppe")
    val diagnoseHovedgruppe: Column<String> = text("diagnose_hovedgruppe")
    val diagnoseUndergruppe: Column<String> = text("diagnose_undergruppe")
    val varighetsgruppe: Column<String> = text("varighetsgruppe")
    val naeringsgruppe: Column<String?> = text("naeringsgruppe").nullable()
    val antallSykmeldinger: Column<Int> = integer("antall_sykmeldinger")
    val gradert: Column<Boolean> = bool("gradert")
    val antallDager: Column<Int> = integer("antall_dager")

    override val primaryKey = PrimaryKey(pk)
}
