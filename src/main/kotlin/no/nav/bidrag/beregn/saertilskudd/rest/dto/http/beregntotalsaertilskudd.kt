package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.math.BigDecimal
import java.time.LocalDate

// Grunnlag
@Schema(description = "Totalgrunnlaget for en særtilskuddsberegning")
data class BeregnTotalSaertilskuddGrunnlag(
        @Schema(description = "Beregn fra-dato") val beregnDatoFra: LocalDate? = null,
        @Schema(description = "Beregn til-dato") val beregnDatoTil: LocalDate? = null,
        @Schema(description = "Periodisert liste over grunnlagselementer") val grunnlagListe: List<Grunnlag>? = null,
) {

    fun valider() {
        if (beregnDatoFra == null) throw UgyldigInputException("beregnDatoFra kan ikke være null")
        if (beregnDatoTil == null) throw UgyldigInputException("beregnDatoTil kan ikke være null")
        if (grunnlagListe != null) grunnlagListe.map { it.valider() } else throw UgyldigInputException("grunnlagListe kan ikke være null")
    }
}

@Schema(description = "Grunnlag")
data class Grunnlag(
        @Schema(description = "Referanse") val referanse: String? = null,
        @Schema(description = "Type") val type: GrunnlagType? = null,
        @Schema(description = "Innhold") val innhold: JsonNode? = null
) {
    fun valider() {
        if (referanse == null) throw UgyldigInputException("referanse kan ikke være null")
        if (type == null) throw UgyldigInputException("type kan ikke være null")
        if (innhold == null) throw UgyldigInputException("innhold kan ikke være null")
    }
}

// Resultat
@Schema(description = "Totalresultatet av en særtilskuddsberegning")
data class BeregnetTotalSaertilskuddResultat(
        @Schema(description = "Periodisert liste over resultat av særtilskuddsberegning") var beregnetSaertilskuddPeriodeListe: List<ResultatPeriode> = emptyList(),
        @Schema(description = "Liste over grunnlag brukt i beregning") var grunnlagListe: List<ResultatGrunnlag> = emptyList()
) {
    constructor(beregnetSaertilskuddResultat: BeregnSaertilskuddResultatCore, grunnlagListe: List<ResultatGrunnlag>) : this(
            beregnetSaertilskuddPeriodeListe = beregnetSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriode(it) },
            grunnlagListe = grunnlagListe)
}

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriode(
        @Schema(description = "Beregnet resultat periode") var periode: Periode = Periode(),
        @Schema(description = "Beregnet resultat innhold") var resultat: ResultatBeregning = ResultatBeregning(),
        @Schema(description = "Beregnet grunnlag innhold") var grunnlagReferanseListe: List<String> = emptyList()
) {
    constructor(resultatPeriode: ResultatPeriodeCore) : this(
            periode = Periode(resultatPeriode.periode),
            resultat = ResultatBeregning(resultatPeriode.resultatListe),
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

@Schema(description = "Grunnlaget for en beregning")
data class ResultatGrunnlag(
        @Schema(description = "Referanse") val referanse: String = "",
        @Schema(description = "Type") val type: String = "",
        @Schema(description = "Innhold") val innhold: JsonNode = ObjectMapper().createObjectNode()
)

@Schema(description = "Resultatet av en total barnebidragsberegning")
data class BeregnTotalSaertilskuddResultat(
        @Schema() var beregnBPBidragsevneResultat: BeregnBPBidragsevneResultat,
        @Schema() var beregnBPAndelSaertilskuddResultat: BeregnBPAndelSaertilskuddResultat,
        @Schema() var beregnBPSamvaersfradragResultat: BeregnBPSamvaersfradragResultat,
        @Schema var beregnSaertilskuddResultat: BeregnSaertilskuddResultat
)

