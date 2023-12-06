package no.nav.sykmelderstatistikk.database.upsertdatabase.sfsdatatest

import no.nav.sykmelderstatistikk.models.KafkaMessageSfsDataTest
import no.nav.sykmelderstatistikk.securelogger
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.upsert

fun handleSfsData(kafkaDiagnoseData: KafkaMessageSfsDataTest) {
    val data = kafkaDiagnoseData.data

    transaction {
        addLogger(StdOutSqlLogger)

        SfsDataTest.upsert {
            it[pk] = data.PK
            it[aarmnd] = data.AARMND
            it[sykmHovedgruppeKode] = data.SYKM_HOVEDGRUPPE_KODE
            it[sykmUndergruppeKode] = data.SYKM_UNDERGRUPPE_KODE
            it[sykmelderSammenlTypeKode] = data.SYKMELDER_SAMMENL_TYPE_KODE
            it[kjonnKode] = data.KJONN_KODE
            it[alder] = data.ALDER
            it[alderGruppe5Besk] = data.ALDER_GRUPPE5_BESK
            it[bydelNr] = data.BYDEL_NR
            it[kommuneNr] = data.KOMMUNE_NR
            it[fylkeNr] = data.FYLKE_NR
            it[alderYrkesaktivFlagg] = data.ALDER_YRKESAKTIV_FLAGG
            it[naeringInntektKategori] = data.NAERING_INNTEKT_KATEGORI
            it[ikkeArbeidstakerFlagg] = data.IKKE_ARBEIDSTAKER_FLAGG
            it[rangering] = data.RANGERING
            it[pasientAntall] = data.PASIENT_ANTALL
            it[arbeidAntall] = data.ARBEID_ANTALL
        }
    }

    securelogger.info("sykmelderStatestikk is: $data")
}
