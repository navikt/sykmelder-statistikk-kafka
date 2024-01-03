package no.nav.sykmelderstatistikk.sfs.database

import SfsSykmelding
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.transactions.transaction

fun upsertSfsSykmelding(sfsSykmelding: List<SfsSykmelding>) {

    transaction {
        SfsSykmeldingTable.batchUpsert(sfsSykmelding) {
            this[SfsSykmeldingTable.pk] = it.pk
            this[SfsSykmeldingTable.ar] = it.ar
            this[SfsSykmeldingTable.mnd] = it.mnd
            this[SfsSykmeldingTable.bydel] = it.sykmelder.bydel
            this[SfsSykmeldingTable.kommune] = it.sykmelder.kommune
            this[SfsSykmeldingTable.fylke] = it.sykmelder.fylke
            this[SfsSykmeldingTable.sykmelderFnr] = it.sykmelder.fnr
            this[SfsSykmeldingTable.hovedgruppeKode] = it.sykmelder.hovedgruppeKode
            this[SfsSykmeldingTable.undergruppeKode] = it.sykmelder.undergruppeKode
            this[SfsSykmeldingTable.sammensattKode] = it.sykmelder.sammensattKode
            this[SfsSykmeldingTable.pasientKjonn] = it.sykmeldt.kjonn
            this[SfsSykmeldingTable.pasientAlder] = it.sykmeldt.alder
            this[SfsSykmeldingTable.diagnoseHovedgruppe] = it.sykmelding.diagnoseHovedgruppe
            this[SfsSykmeldingTable.diagnoseUndergruppe] = it.sykmelding.diagnoseUndergruppe
            this[SfsSykmeldingTable.naeringsgruppe] = it.sykmeldt.naeringsgruppe
            this[SfsSykmeldingTable.inntektskategori] = it.sykmeldt.inntektKategori
            this[SfsSykmeldingTable.gradert] = it.sykmelding.gradert
            this[SfsSykmeldingTable.antallDager] = it.sykmelding.antallDager
            this[SfsSykmeldingTable.sykefravarFom] = it.sykmelding.sykefravarFom
            this[SfsSykmeldingTable.fom] = it.sykmelding.fom
            this[SfsSykmeldingTable.tom] = it.sykmelding.tom
        }
    }
}
