package no.nav.sykmelderstatistikk.sfs

import SfsSykmelding
import SykmeldingVarighet
import no.nav.sykmelderstatistikk.database.upsertdatabase.sykmeldingvarighet.upsertSykmeldingVarighet
import no.nav.sykmelderstatistikk.sfs.database.upsertSfsSykmelding

class SfsDataService {
    fun updateData(sykmeldingVarighetList: List<SykmeldingVarighet>) {
        upsertSykmeldingVarighet(sykmeldingVarighetList)
    }

    fun sfsSykmelding(sfsSykmelding: List<SfsSykmelding>) {
        upsertSfsSykmelding(sfsSykmelding)
    }
}
