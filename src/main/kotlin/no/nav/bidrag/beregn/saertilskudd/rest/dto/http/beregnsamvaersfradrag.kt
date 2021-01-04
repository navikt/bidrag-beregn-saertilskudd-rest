package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import no.nav.bidrag.beregn.samvaersfradrag.dto.BeregnSamvaersfradragResultatCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.GrunnlagBeregningPeriodisertCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersfradragGrunnlagPerBarnCore
import no.nav.bidrag.beregn.samvaersfradrag.dto.SamvaersklassePeriodeCore
import java.math.BigDecimal
import java.time.LocalDate

// Grunnlag
@ApiModel(value = "Grunnlaget for en samværsfradragberegning for bidragspliktig")
data class BeregnBPSamvaersfradragGrunnlag(
    @ApiModelProperty(
        value = "Periodisert liste over bidragspliktiges samværsklasser") val samvaersklassePeriodeListe: List<SamvaersklassePeriode>? = null
)

@ApiModel(value = "Bidragspliktiges samværsklasse")
data class SamvaersklassePeriode(
    @ApiModelProperty(value = "Bidragspliktiges samværsklasse fra-til-dato") var samvaersklasseDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Barn person-id") var samvaersklasseBarnPersonId: Int? = null,
    @ApiModelProperty(value = "Barn fødselsdato") var samvaersklasseBarnFodselsdato: LocalDate? = null,
    @ApiModelProperty(value = "Bidragspliktiges samværsklasse id") var samvaersklasseId: String? = null
) {

  fun tilCore() = SamvaersklassePeriodeCore(
      samvaersklassePeriodeDatoFraTil = if (samvaersklasseDatoFraTil != null) samvaersklasseDatoFraTil!!.tilCore(
          "samvaersklasse") else throw UgyldigInputException("samvaersklasseDatoFraTil kan ikke være null"),
      barnPersonId = if (samvaersklasseBarnPersonId != null) samvaersklasseBarnPersonId!! else throw UgyldigInputException(
          "samvaersklasseBarnPersonId kan ikke være null"),
      barnFodselsdato = if (samvaersklasseBarnFodselsdato != null) samvaersklasseBarnFodselsdato!! else throw UgyldigInputException(
          "samvaersklasseBarnFodselsdato kan ikke være null"),
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
      resultatPeriodeListe = beregnSamvaersfradragResultat.resultatPeriodeListe.map { ResultatPeriodeSamvaersfradrag(it) }
  )
}

@ApiModel(value = "Resultatet av beregning av samværsfradrag for et søknadsbarn for en gitt periode")
data class ResultatPeriodeSamvaersfradrag(
  @ApiModelProperty(value = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
  @ApiModelProperty(value = "Beregning resultat innhold liste") var resultatBeregningListe: List<ResultatBeregningSamvaersfradrag> = emptyList(),
  @ApiModelProperty(value = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagSamvaersfradrag = ResultatGrunnlagSamvaersfradrag()
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregningListe = resultatPeriode.resultatBeregningListe.map { ResultatBeregningSamvaersfradrag(it) },
      resultatGrunnlag = ResultatGrunnlagSamvaersfradrag(resultatPeriode.resultatGrunnlag)
  )
}

@ApiModel(value = "Resultatet av beregning av samværsfradrag")
data class ResultatBeregningSamvaersfradrag(
    @ApiModelProperty(value = "Barn person-id") var barnPersonId: Int? = null,
    @ApiModelProperty(value = "Beløp samværsfradrag") var resultatBelop: BigDecimal = BigDecimal.ZERO
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      barnPersonId = resultatBeregning.barnPersonId,
      resultatBelop = resultatBeregning.resultatSamvaersfradragBelop
  )
}

@ApiModel(value = "Grunnlaget for beregning av samværsfradrag")
data class ResultatGrunnlagSamvaersfradrag(
    @ApiModelProperty(value = "Liste over grunnlag pr barn") var grunnlagBarnListe: List<GrunnlagBarn> = emptyList(),
    @ApiModelProperty(value = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

  constructor(resultatGrunnlag: GrunnlagBeregningPeriodisertCore) : this(
      grunnlagBarnListe = resultatGrunnlag.samvaersfradragGrunnlagPerBarnListe.map { GrunnlagBarn(it) },
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}

@ApiModel(value = "Grunnlaget for beregning av samværsfradrag - pr barn")
data class GrunnlagBarn(
    @ApiModelProperty(value = "Barn person-id") var barnPersonId: Int = 0,
    @ApiModelProperty(value = "Barn alder") var barnAlder: Int = 0,
    @ApiModelProperty(value = "Samværsklasse Id") var samvaersklasseId: String = ""
) {

  constructor(resultatGrunnlagBarn: SamvaersfradragGrunnlagPerBarnCore) : this(
      barnPersonId = resultatGrunnlagBarn.barnPersonId,
      barnAlder = resultatGrunnlagBarn.barnAlder,
      samvaersklasseId = resultatGrunnlagBarn.samvaersklasse
  )
}
