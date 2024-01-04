import java.time.LocalDate
import no.nav.sykmelderstatistikk.sfs.kafka.model.FakSfsSykmelding

data class SfsSykmelding(
    val pk: Int,
    val ar: Int,
    val mnd: Int,
    val sykmelder: Sykmelder,
    val sykmelding: Sykmelding,
    val sykmeldt: Sykmeldt,
)

data class Sykmelder(
    val fnr: String,
    val hovedgruppeKode: String,
    val undergruppeKode: String,
    val sammensattKode: String,
    val bydel: String?,
    val kommune: String,
    val fylke: String,
)

data class Sykmelding(
    val sykefravarFom: LocalDate,
    val fom: LocalDate,
    val tom: LocalDate,
    val grad: Int,
    val antallDager: Int,
    val gradert: Boolean,
    val diagnoseHovedgruppe: String,
    val diagnoseUndergruppe: String,
)

data class Sykmeldt(
    val naeringsgruppe: String?,
    val inntektKategori: String,
    val kjonn: String,
    val alder: Int,
)

fun toSfsSykmelding(sfsSykmelding: FakSfsSykmelding): SfsSykmelding {
    val ar = sfsSykmelding.AARMND.substring(0, 4).toInt()
    val mnd = sfsSykmelding.AARMND.substring(4).toInt()
    val bydel =
        when (sfsSykmelding.SYKM_BYDEL_NAVN.lowercase().trim()) {
            "ukjent" -> null
            "n/a" -> null
            else -> sfsSykmelding.SYKM_BYDEL_NAVN
        }
    val gradert =
        when (sfsSykmelding.GRADERT_FLAGG) {
            1 -> true
            else -> false
        }
    return SfsSykmelding(
        pk = sfsSykmelding.PK,
        ar = ar,
        mnd = mnd,
        sykmelder =
            Sykmelder(
                fnr = sfsSykmelding.SYKM_FODSEL_NR,
                hovedgruppeKode = sfsSykmelding.SYKM_HOVEDGRUPPE_KODE,
                undergruppeKode = sfsSykmelding.SYKM_UNDERGRUPPE_KODE,
                sammensattKode = sfsSykmelding.SYKMELDER_SAMMENL_TYPE_KODE,
                bydel = bydel,
                kommune = sfsSykmelding.SYKM_KOMMUNE_NAVN,
                fylke = sfsSykmelding.SYKM_FYLKE_NAVN,
            ),
        sykmelding =
            Sykmelding(
                sykefravarFom = sfsSykmelding.SYKEFRAVAR_FRA_DATO,
                fom = sfsSykmelding.SYKMELDT_FRA_DATO,
                tom = sfsSykmelding.SYKMELDT_TIL_DATO,
                grad = sfsSykmelding.GRAD,
                antallDager = sfsSykmelding.VARIGHET_DAGER,
                gradert = gradert,
                diagnoseHovedgruppe = sfsSykmelding.HOVEDGRUPPE_SMP_BESK,
                diagnoseUndergruppe = sfsSykmelding.UNDERGRUPPE_SMP_BESK,
            ),
        sykmeldt =
            Sykmeldt(
                naeringsgruppe = sfsSykmelding.NAERING_GRUPPE6_BESK_LANG,
                inntektKategori = sfsSykmelding.INNTEKT_KATEGORI_NAVN,
                kjonn = sfsSykmelding.PASIENT_KJONN_KODE,
                alder = sfsSykmelding.PASIENT_ALDER,
            )
    )
}
