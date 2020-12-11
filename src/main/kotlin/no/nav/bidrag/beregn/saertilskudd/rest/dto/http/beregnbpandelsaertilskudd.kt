package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.NettoSaertilskuddPeriodeCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.math.BigDecimal

// Grunnlag
@ApiModel(value = "Grunnlaget for en beregning av bidragspliktiges andel av særtilskudd")
data class BeregnBPAndelSaertilskuddGrunnlag(
    @ApiModelProperty(
        value = "Periodisert liste over bidragspliktiges netto særtilskudd") val nettoSaertilskuddPeriodeListe: List<NettoSaertilskuddPeriode>? = null
)

@ApiModel(value = "Bidragspliktiges netto særtilskudd")
data class NettoSaertilskuddPeriode(
    @ApiModelProperty(value = "Bidragspliktiges netto særtilskudd fra-til-dato") var nettoSaertilskuddDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Bidragspliktiges netto særtilskudd beløp") var nettoSaertilskuddBelop: BigDecimal? = null
) {

  fun tilCore() = NettoSaertilskuddPeriodeCore(
      periodeDatoFraTil = if (nettoSaertilskuddDatoFraTil != null) nettoSaertilskuddDatoFraTil!!.tilCore(
          "nettoSaertilskudd") else throw UgyldigInputException("nettoSaertilskuddDatoFraTil kan ikke være null"),
      nettoSaertilskuddBelop = if (nettoSaertilskuddBelop != null)
        nettoSaertilskuddBelop!! else throw UgyldigInputException("nettoSaertilskuddBelop kan ikke være null")
  )
}

// Resultat
@ApiModel(value = "Resultatet av en beregning av bidragspliktiges andel av særtilskudd")
data class BeregnBPAndelSaertilskuddResultat(
    @ApiModelProperty(value = "Periodisert liste over resultat av beregning av bidragspliktiges andel av særtilskudd")
    var resultatPeriodeListe: List<ResultatPeriodeBPAndelSaertilskudd> = emptyList()
) {

  constructor(beregnBPAndelSaertilskuddResultat: BeregnBPsAndelSaertilskuddResultatCore) : this(
      resultatPeriodeListe = beregnBPAndelSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriodeBPAndelSaertilskudd(it) }
  )
}

@ApiModel(value = "Resultatet av beregning av bidragspliktiges andel av særtilskudd for en gitt periode")
data class ResultatPeriodeBPAndelSaertilskudd(
    @ApiModelProperty(value = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode,
    @ApiModelProperty(value = "Beregning resultat innhold") var resultatBeregning: ResultatBeregningBPAndelSaertilskudd,
    @ApiModelProperty(value = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagBPAndelSaertilskudd
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregning = ResultatBeregningBPAndelSaertilskudd(resultatPeriode.resultatBeregning),
      resultatGrunnlag = ResultatGrunnlagBPAndelSaertilskudd(resultatPeriode.resultatGrunnlag)
  )
}

@ApiModel(value = "Resultatet av beregning av bidragspliktiges andel av særtilskudd")
data class ResultatBeregningBPAndelSaertilskudd(
    @ApiModelProperty(value = "Resultatandel prosent") var resultatAndelProsent: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Resultatandel beløp") var resultatAndelBelop: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Barnet er selvforsørget") var barnetErSelvforsorget: Boolean = false
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatAndelProsent = resultatBeregning.resultatAndelProsent,
      resultatAndelBelop = resultatBeregning.resultatAndelBelop,
      barnetErSelvforsorget = resultatBeregning.barnetErSelvforsorget
  )
}

@ApiModel(value = "Grunnlaget for beregning av bidragspliktiges andel av særtilskudd")
data class ResultatGrunnlagBPAndelSaertilskudd(
    @ApiModelProperty(value = "Netto særtilskudd beløp") var nettoSaertilskuddBelop: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Liste over bidragspliktiges inntekter") var inntektBPListe: List<Inntekt> = emptyList(),
    @ApiModelProperty(value = "Liste over bidragsmottakers inntekter") var inntektBMListe: List<InntektBM> = emptyList(),
    @ApiModelProperty(value = "Liste over søknadsbarnets inntekter") var inntektSBListe: List<Inntekt> = emptyList(),
    @ApiModelProperty(value = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      nettoSaertilskuddBelop = resultatGrunnlag.nettoSaertilskuddBelop,
      inntektBPListe = resultatGrunnlag.inntektBPListe.map { Inntekt(it) },
      inntektBMListe = resultatGrunnlag.inntektBMListe.map { InntektBM(it) },
      inntektSBListe = resultatGrunnlag.inntektBBListe.map { Inntekt(it) },
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}
