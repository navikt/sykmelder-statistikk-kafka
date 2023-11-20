package no.nav.sykmelderstatistikk.sfsdataalle

import no.nav.sykmelderstatistikk.securelogger
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

fun handleSfsDataAlle(kafkaDiagnoseData: KafkaMessageSfsDataAlle) {
    val data = kafkaDiagnoseData.data

    transaction {
        addLogger(StdOutSqlLogger)

        SfsDataAlle.insert {
            it[pk] = data.PK
            it[aarmnd] = data.AARMND
            it[sykm_bydel_navn] = data.SYKM_BYDEL_NAVN
            it[sykm_kommune_navn] = data.SYKM_KOMMUNE_NAVN
            it[sykm_fylke_navn] = data.SYKM_FYLKE_NAVN
            it[sykm_hovedgruppe_kode] = data.SYKM_HOVEDGRUPPE_KODE
            it[sykm_undergruppe_kode] = data.SYKM_UNDERGRUPPE_KODE
            it[sykmelder_sammenl_type_kode] = data.SYKMELDER_SAMMENL_TYPE_KODE
            it[pasient_kjonn_kode] = data.PASIENT_KJONN_KODE
            it[pasient_alder_gruppe7_besk] = data.PASIENT_ALDER_GRUPPE7_BESK
            it[hovedgruppe_smp_besk] = data.HOVEDGRUPPE_SMP_BESK
            it[undergruppe_smp_besk] = data.UNDERGRUPPE_SMP_BESK
            it[varighet_gruppe9_besk] = data.VARIGHET_GRUPPE9_BESK
            it[naering_gruppe6_besk_lang] = data.NAERING_GRUPPE6_BESK_LANG
            it[antall_sykmeldinger] = data.ANTALL_SYKMELDINGER
            it[gradert_flagg] = data.GRADERT_FLAGG
            it[antall_dager] = data.ANTALL_DAGER
        }
    }

    securelogger.info("sykmelderStatestikk is: $data")
}
