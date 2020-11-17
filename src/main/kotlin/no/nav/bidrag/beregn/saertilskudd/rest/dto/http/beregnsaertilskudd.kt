package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.time.LocalDate

// Grunnlag
@ApiModel(value = "Grunnlaget for en særtilskuddsberegning")
data class BeregnSaertilskuddGrunnlag(
    @ApiModelProperty(value = "Beregn fra-dato") var beregnDatoFra: LocalDate? = null,
    @ApiModelProperty(value = "Beregn til-dato") var beregnDatoTil: LocalDate? = null,
    @ApiModelProperty(value = "Grunnlag") var grunnlag: String? = null
) {

  fun tilCore() = BeregnSaertilskuddGrunnlagCore(
      beregnDatoFra = if (beregnDatoFra != null) beregnDatoFra!! else throw UgyldigInputException("beregnDatoFra kan ikke være null"),
      beregnDatoTil = if (beregnDatoTil != null) beregnDatoTil!! else throw UgyldigInputException("beregnDatoTil kan ikke være null"),
      grunnlag = if (grunnlag != null) grunnlag!! else throw UgyldigInputException("grunnlag kan ikke være null")
  )
}

// Resultat
@ApiModel(value = "Resultatet av en særtilskuddsberegning")
data class BeregnSaertilskuddResultat(
    @ApiModelProperty(value = "Resultat") var resultat: String = " "
) {

  constructor(saertilskuddResultat: BeregnSaertilskuddResultatCore) : this(
      resultat = saertilskuddResultat.resultat
  )
}
