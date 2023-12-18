import no.nav.sykmelderstatistikk.sfs.kafka.model.AggSfsVarighetEgen

data class SykmeldingVarighet(
    val pk: Int,
    val ar: Int,
    val mnd: Int,
    val bydel: String?,
    val kommune: String,
    val fylke: String,
    val sykmelderFnr: String,
    val hovedgruppeKode: String,
    val undergruppeKode: String,
    val samensattKode: String,
    val pasientKjonn: String,
    val pasientAldersgruppe: String,
    val diagnoseHovedgruppe: String,
    val diagnoseUndergruppe: String,
    val varighetsgruppe: String,
    val naeringsgruppe: String?,
    val antallSykmeldinger: Int,
    val gradert: Boolean,
    val antallDager: Int
)

fun toSykmeldingVarighet(aggSfsVarighetEgen: AggSfsVarighetEgen): SykmeldingVarighet {
    val ar = aggSfsVarighetEgen.AARMND.substring(0, 4).toInt()
    val mnd = aggSfsVarighetEgen.AARMND.substring(4).toInt()
    val bydel =
        when (aggSfsVarighetEgen.SYKM_BYDEL_NAVN.lowercase().trim()) {
            "ukjent" -> null
            "n/a" -> null
            else -> aggSfsVarighetEgen.SYKM_BYDEL_NAVN
        }
    val gradert =
        when (aggSfsVarighetEgen.GRADERT_FLAGG) {
            1 -> true
            else -> false
        }
    return SykmeldingVarighet(
        pk = aggSfsVarighetEgen.PK,
        ar = ar,
        mnd = mnd,
        bydel = bydel,
        kommune = aggSfsVarighetEgen.SYKM_KOMMUNE_NAVN,
        fylke = aggSfsVarighetEgen.SYKM_KOMMUNE_NAVN,
        sykmelderFnr = aggSfsVarighetEgen.SYKM_FODSEL_NR,
        hovedgruppeKode = aggSfsVarighetEgen.SYKM_HOVEDGRUPPE_KODE,
        undergruppeKode = aggSfsVarighetEgen.SYKM_UNDERGRUPPE_KODE,
        samensattKode = aggSfsVarighetEgen.SYKMELDER_SAMMENL_TYPE_KODE,
        pasientKjonn = aggSfsVarighetEgen.PASIENT_KJONN_KODE,
        pasientAldersgruppe = aggSfsVarighetEgen.PASIENT_ALDER_GRUPPE7_BESK,
        diagnoseHovedgruppe = aggSfsVarighetEgen.HOVEDGRUPPE_SMP_BESK,
        diagnoseUndergruppe = aggSfsVarighetEgen.UNDERGRUPPE_SMP_BESK,
        varighetsgruppe = aggSfsVarighetEgen.VARIGHET_GRUPPE9_BESK,
        naeringsgruppe = aggSfsVarighetEgen.NAERING_GRUPPE6_BESK_LANG,
        antallSykmeldinger = aggSfsVarighetEgen.ANTALL_SYKMELDINGER,
        gradert = gradert,
        antallDager = aggSfsVarighetEgen.ANTALL_DAGER
    )
}
