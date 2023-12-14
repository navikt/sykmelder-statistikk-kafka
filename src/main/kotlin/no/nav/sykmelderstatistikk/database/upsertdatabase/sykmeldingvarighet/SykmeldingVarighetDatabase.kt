package no.nav.sykmelderstatistikk.database.upsertdatabase.sykmeldingvarighet

import SykmeldingVarighet
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

fun upsertSykmeldingVarighet(sykmeldingVarighet: SykmeldingVarighet) {

    transaction {
        SykmeldingVarighetTable.upsert {
            it[pk] = sykmeldingVarighet.pk
            it[ar] = sykmeldingVarighet.ar
            it[bydel] = sykmeldingVarighet.bydel
            it[kommune] = sykmeldingVarighet.kommune
            it[fylke] = sykmeldingVarighet.fylke
            it[hovedgruppeKode] = sykmeldingVarighet.hovedgruppeKode
            it[undergruppeKode] = sykmeldingVarighet.undergruppeKode
            it[sammensattKode] = sykmeldingVarighet.samensattKode
            it[pasientKjonn] = sykmeldingVarighet.pasientKjonn
            it[pasientAldersgruppe] = sykmeldingVarighet.pasientAldersgruppe
            it[diagnoseHovedgruppe] = sykmeldingVarighet.diagnoseHovedgruppe
            it[diagnoseUndergruppe] = sykmeldingVarighet.diagnoseUndergruppe
            it[varighetsgruppe] = sykmeldingVarighet.varighetsgruppe
            it[naeringsgruppe] = sykmeldingVarighet.naeringsgruppe
            it[antallSykmeldinger] = sykmeldingVarighet.antallSykmeldinger
            it[gradert] = sykmeldingVarighet.gradert
            it[antallDager] = sykmeldingVarighet.antallDager
        }
    }
}
