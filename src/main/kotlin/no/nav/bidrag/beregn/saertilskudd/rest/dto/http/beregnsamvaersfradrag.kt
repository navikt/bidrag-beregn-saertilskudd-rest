package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore
import java.math.BigDecimal

// Grunnlag
@ApiModel(value = "Grunnlaget for en samværsfradragberegning for bidragspliktig")
data class BeregnBPSamvaersfradragGrunnlag(
    @ApiModelProperty(
        value = "Periodisert liste over bidragspliktiges samværsklasser") val samvaersklassePeriodeListe: List<SamvaersklassePeriode>? = null
)

@ApiModel(value = "Bidragspliktiges samværsklasse")
data class SamvaersklassePeriode(
    @ApiModelProperty(value = "Bidragspliktiges samværsklasse fra-til-dato") var samvaersklasseDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Bidragspliktiges samværsklasse id") var samvaersklasseId: String? = null
) {

  fun tilCore() = SamvaersklassePeriodeCore(
      samvaersklassePeriodeDatoFraTil = if (samvaersklasseDatoFraTil != null) samvaersklasseDatoFraTil!!.tilCore(
          "samvaersklasse") else throw UgyldigInputException("samvaersklasseDatoFraTil kan ikke være null"),
      samvaersklasse = if (samvaersklasseId != null) samvaersklasseId!! else throw UgyldigInputException("samvaersklasseId kan ikke være null")
  )
}


// Resultat
@ApiModel(value = "Resultatet av en samværsfradragberegning for bidragspliktig")
data class BeregnBPSamvaersfradragResultat(
    @ApiModelProperty(
        value = "Periodisert liste over resultat av beregning av samværsfradrag") var resultatPeriodeListe: List<ResultatPeriodeSamvaersfradrag> = emptyList()
) {

  constructor(beregnSamvaersfradragResultat: BeregnSamvaersfradragResultatCore) : this(
      resultatPeriodeListe = beregnSamvaersfradragResultat.resultatPeriodeListe.map {ResultatPeriodeSamvaersfradrag(it) }
  )
}

@ApiModel(value = "Resultatet av beregning av samværsfradrag for et søknadsbarn for en gitt periode")
data class ResultatPeriodeSamvaersfradrag(
    @ApiModelProperty(value = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode,
    @ApiModelProperty(value = "Beregning resultat innhold") var resultatBeregning: ResultatBeregningSamvaersfradrag,
    @ApiModelProperty(value = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagSamvaersfradrag
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregning = ResultatBeregningSamvaersfradrag(resultatPeriode.resultatBeregning),
      resultatGrunnlag = ResultatGrunnlagSamvaersfradrag(resultatPeriode.resultatGrunnlag)
  )
}

@ApiModel(value = "Resultatet av beregning av samværsfradrag")
data class ResultatBeregningSamvaersfradrag(
    @ApiModelProperty(value = "Beløp samværsfradrag") var resultatBelop: BigDecimal = BigDecimal.ZERO
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatBelop = resultatBeregning.resultatSamvaersfradragBelop
  )
}

@ApiModel(value = "Grunnlaget for beregning av samværsfradrag")
data class ResultatGrunnlagSamvaersfradrag(
    @ApiModelProperty(value = "Søknadsbarnets alder") var soknadsbarnAlder: Int = 0,
    @ApiModelProperty(value = "Samværsklasse Id") var samvaersklasseId: String,
    @ApiModelProperty(value = "Liste over sjablonperioder") var sjablonListe: List<Sjablon>
) {

  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      soknadsbarnAlder = resultatGrunnlag.soknadBarnAlder,
      samvaersklasseId = resultatGrunnlag.samvaersklasse,
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}
