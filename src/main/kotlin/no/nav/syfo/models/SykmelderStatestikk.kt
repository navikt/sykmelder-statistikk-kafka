package no.nav.syfo.no.nav.syfo.models

data class SykmelderStatestikk(
    val primaryKey: Int,
    val houvedGruppe: HouvedGruppe,
    val underGruppe: UnderGruppe,
    val type: Type,
    val kjonn: Kjonn,
    val alder: Int,
    val aar: String,
    val mnd: String,
    val bydelNr: String?,
    val fylkeNr: String,
    val kommuneNr: String,
    val aarYrkesAktiv: Int,
    val naringInntektKategori: String,
    val ikkeArbeidstaker: Boolean,
    val rangering: Int,
    val antallPasienter: Int,
    val antallPasienterIArbeid: Int,
)

enum class Kjonn {
    M,
    K
}

enum class Type {
    ALLM
}

enum class UnderGruppe {
    FAST
}

enum class HouvedGruppe {
    ALLM
}
