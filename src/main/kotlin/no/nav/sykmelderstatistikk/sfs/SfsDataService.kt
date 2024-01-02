package no.nav.sykmelderstatistikk.sfs

import SfsSykmelding
import SykmeldingVarighet
import no.nav.sykmelderstatistikk.database.upsertdatabase.sykmeldingvarighet.upsertSykmeldingVarighet

class SfsDataService {
    fun updateData(sykmeldingVarighetList: List<SykmeldingVarighet>) {
        upsertSykmeldingVarighet(sykmeldingVarighetList)
    }
    fun updateData(sfsSykmelding: List<SfsSykmelding>) {
        // TODO
    }
}
