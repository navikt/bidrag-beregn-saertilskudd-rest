package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore
import java.math.BigDecimal
import java.time.LocalDate

data class BeregnetTotalSaertilskuddResultat(
    @Schema(description = "Periodisert liste over resultat av barnebidragsberegning") var beregnetSaertilskuddPeriodeListe: List<ResultatPeriode> = emptyList(),
    @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<Grunnlag> = emptyList()
) {

    constructor(beregnetSaertilskuddResultatCore: BeregnSaertilskuddResultatCore, grunnlagListe: List<Grunnlag>) : this(
        beregnetSaertilskuddPeriodeListe = beregnetSaertilskuddResultatCore.resultatPeriodeListe.map { ResultatPeriode(it) },
        grunnlagListe = grunnlagListe
    )
}

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriode(
    @Schema(description = "Søknadsbarn") var barn: Int = 0,
    @Schema(description = "Beregnet resultat periode") var periode: Periode = Periode(),
    @Schema(description = "Beregnet resultat innhold") var resultat: ResultatBeregning = ResultatBeregning(),
    @Schema(description = "Beregnet grunnlag innhold") var grunnlagReferanseListe: List<String> = emptyList()
) {

    constructor(resultatPeriode: ResultatPeriodeCore) : this(
        barn = resultatPeriode.soknadsbarnPersonId,
        periode = Periode(resultatPeriode.periode),
        resultat = ResultatBeregning(resultatPeriode.resultat),
        grunnlagReferanseListe = resultatPeriode.grunnlagReferanseListe
    )
}

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregning(
    @Schema(description = "Resultat beløp") var belop: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Resultat kode") var kode: String = ""
) {

    constructor(resultatBeregning: ResultatBeregningCore) : this(
        belop = resultatBeregning.belop,
        kode = resultatBeregning.kode
    )
}

class BidragsevneResultatPeriode(datoFom: LocalDate, datoTil: LocalDate, val belop: BigDecimal, val grunnlagReferanseListe: List<String>) :
    BasePeriode(datoFom, datoTil)

class BPsAndelSaertilskuddResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val belop: BigDecimal,
    val prosent: BigDecimal,
    val selvforsorget: Boolean,
    val grunnlagReferanseListe: List<String>
) : BasePeriode(datoFom, datoTil)

class SamvaersfradragResultatPeriode(
    datoFom: LocalDate,
    datoTil: LocalDate,
    val belop: BigDecimal,
    val barn: Int,
    val grunnlagReferanseListe: List<String>
) : BasePeriode(datoFom, datoTil)

class SjablonResultatPeriode(datoFom: LocalDate, datoTil: LocalDate, val sjablonNavn: String, val sjablonVerdi: Int) : BasePeriode(datoFom, datoTil)
