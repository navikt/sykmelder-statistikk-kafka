package no.nav.sykmelderstatistikk.database.upsertdatabase.sykmeldingvarighet

import SykmeldingVarighet
import org.jetbrains.exposed.sql.batchUpsert
import org.jetbrains.exposed.sql.transactions.transaction

fun upsertSykmeldingVarighet(sykmeldingVarighetList: List<SykmeldingVarighet>) {

    transaction {
        SykmeldingVarighetTable.batchUpsert(sykmeldingVarighetList) {
            this[SykmeldingVarighetTable.pk] = it.pk
            this[SykmeldingVarighetTable.ar] = it.ar
            this[SykmeldingVarighetTable.mnd] = it.mnd
            this[SykmeldingVarighetTable.sykmelderFnr] = it.sykmelderFnr
            this[SykmeldingVarighetTable.bydel] = it.bydel
            this[SykmeldingVarighetTable.kommune] = it.kommune
            this[SykmeldingVarighetTable.fylke] = it.fylke
            this[SykmeldingVarighetTable.hovedgruppeKode] = it.hovedgruppeKode
            this[SykmeldingVarighetTable.undergruppeKode] = it.undergruppeKode
            this[SykmeldingVarighetTable.sammensattKode] = it.samensattKode
            this[SykmeldingVarighetTable.pasientKjonn] = it.pasientKjonn
            this[SykmeldingVarighetTable.pasientAldersgruppe] = it.pasientAldersgruppe
            this[SykmeldingVarighetTable.diagnoseHovedgruppe] = it.diagnoseHovedgruppe
            this[SykmeldingVarighetTable.diagnoseUndergruppe] = it.diagnoseUndergruppe
            this[SykmeldingVarighetTable.varighetsgruppe] = it.varighetsgruppe
            this[SykmeldingVarighetTable.naeringsgruppe] = it.naeringsgruppe
            this[SykmeldingVarighetTable.antallSykmeldinger] = it.antallSykmeldinger
            this[SykmeldingVarighetTable.gradert] = it.gradert
            this[SykmeldingVarighetTable.antallDager] = it.antallDager
        }
    }
}
