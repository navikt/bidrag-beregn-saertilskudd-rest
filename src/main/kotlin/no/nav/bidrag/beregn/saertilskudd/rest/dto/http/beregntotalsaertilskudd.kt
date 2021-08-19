package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import com.fasterxml.jackson.databind.JsonNode
import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
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
@Schema(description = "Resultatet av en total barnebidragsberegning")
data class BeregnTotalSaertilskuddResultat(
        @Schema() var beregnBPBidragsevneResultat: BeregnBPBidragsevneResultat,
        @Schema() var beregnBPAndelSaertilskuddResultat: BeregnBPAndelSaertilskuddResultat,
        @Schema() var beregnBPSamvaersfradragResultat: BeregnBPSamvaersfradragResultat,
        @Schema var beregnSaertilskuddResultat: BeregnSaertilskuddResultat
)

