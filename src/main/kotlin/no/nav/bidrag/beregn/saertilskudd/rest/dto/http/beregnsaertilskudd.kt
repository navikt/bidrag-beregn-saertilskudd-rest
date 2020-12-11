package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevneCore
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragCore
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragCore
import no.nav.bidrag.beregn.saertilskudd.rest.exception.UgyldigInputException
import java.math.BigDecimal

// Grunnlag
@ApiModel(value = "Grunnlaget for en beregning av særtilskudd")
data class BeregnSaertilskuddGrunnlag(
    @ApiModelProperty(value = "Periodisert liste over bidragspliktiges løpende bidrag")
    val lopendeBidragBPPeriodeListe: List<LopendeBidragBPPeriode>? = null
)

@ApiModel(value = "Løpende bidrag bidragspliktig")
data class LopendeBidragBPPeriode(
    @ApiModelProperty(value = "Løpende bidrag fra-til-dato") var lopendeBidragDatoFraTil: Periode? = null,
    @ApiModelProperty(value = "Barn person-id") var lopendeBidragBarnPersonId: Int? = null,
    @ApiModelProperty(value = "Løpende bidrag beløp") var lopendeBidragBelop: BigDecimal? = null,
    @ApiModelProperty(value = "Opprinnelig BP andel av underholdskostnad beløp") var opprinneligBPAndelUnderholdskostnadBelop: BigDecimal? = null,
    @ApiModelProperty(value = "Opprinnelig samværsfradrag beløp") var opprinneligSamvaersfradragBelop: BigDecimal? = null,
    @ApiModelProperty(value = "Opprinnelig bidrag beløp") var opprinneligBidragBelop: BigDecimal? = null
) {

  fun tilCore() = LopendeBidragPeriodeCore(
      periodeDatoFraTil = if (lopendeBidragDatoFraTil != null) lopendeBidragDatoFraTil!!.tilCore(
          "lopendeBidrag") else throw UgyldigInputException("lopendeBidragDatoFraTil kan ikke være null"),
      barnPersonId = if (lopendeBidragBarnPersonId != null) lopendeBidragBarnPersonId!! else throw UgyldigInputException(
          "lopendeBidragBarnPersonId kan ikke være null"),
      lopendeBidragBelop = if (lopendeBidragBelop != null) lopendeBidragBelop!! else throw UgyldigInputException(
          "lopendeBidragBelop kan ikke være null"),
      opprinneligBPsAndelUnderholdskostnadBelop = if (opprinneligBPAndelUnderholdskostnadBelop != null) opprinneligBPAndelUnderholdskostnadBelop!!
      else throw UgyldigInputException("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null"),
      opprinneligSamvaersfradragBelop = if (opprinneligSamvaersfradragBelop != null) opprinneligSamvaersfradragBelop!!
      else throw UgyldigInputException("opprinneligSamvaersfradragBelop kan ikke være null"),
      opprinneligBidragBelop = if (opprinneligBidragBelop != null) opprinneligBidragBelop!!  else throw UgyldigInputException(
          "opprinneligBidragBelop kan ikke være null")
  )
}


// Resultat
@ApiModel(value = "Resultatet av en beregning av særtilskudd")
data class BeregnSaertilskuddResultat(
    @ApiModelProperty(
        value = "Periodisert liste over resultat av beregning av særtilskudd") var resultatPeriodeListe: List<ResultatPeriodeSaertilskudd> = emptyList()
) {

  constructor(beregnSaertilskuddResultat: BeregnSaertilskuddResultatCore) : this(
      resultatPeriodeListe = beregnSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriodeSaertilskudd(it) }
  )
}

@ApiModel(value = "Resultatet av beregning av særtilskudd for en gitt periode")
data class ResultatPeriodeSaertilskudd(
    @ApiModelProperty(value = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode,
    @ApiModelProperty(value = "Beregning resultat innhold liste") var resultatBeregning: ResultatBeregningSaertilskudd,
    @ApiModelProperty(value = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagSaertilskudd
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregning = ResultatBeregningSaertilskudd(resultatPeriode.resultatBeregning),
      resultatGrunnlag = ResultatGrunnlagSaertilskudd(resultatPeriode.resultatGrunnlag)
  )
}

@ApiModel(value = "Resultatet av beregning av særtilskudd")
data class ResultatBeregningSaertilskudd(
    @ApiModelProperty(value = "Beløp særtilskudd") var resultatBelop: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Resultatkode særtilskudd") var resultatKode: String
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatBelop = resultatBeregning.resultatBelop,
      resultatKode = resultatBeregning.resultatkode
  )
}

@ApiModel(value = "Grunnlaget for beregning av særtilskudd")
data class ResultatGrunnlagSaertilskudd(
    @ApiModelProperty(value = "Bidragsevne") var bidragsevneGrunnlag: BidragsevneGrunnlag,
    @ApiModelProperty(value = "BPs andel særtilskudd") var bpAndelSaertilskuddGrunnlag: BPAndelSaertilskuddGrunnlag,
    @ApiModelProperty(value = "Liste over samværsfradrag") var samvaersfradragGrunnlagListe: List<SamvaersfradragGrunnlag> = emptyList(),
    @ApiModelProperty(value = "Liste over løpende bidrag") var lopendeBidragGrunnlagListe: List<LopendeBidragGrunnlag> = emptyList()
) {

  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      bidragsevneGrunnlag = BidragsevneGrunnlag(resultatGrunnlag.bidragsevne),
      bpAndelSaertilskuddGrunnlag = BPAndelSaertilskuddGrunnlag(resultatGrunnlag.bPsAndelSaertilskudd),
      samvaersfradragGrunnlagListe = resultatGrunnlag.samvaersfradragListe.map { SamvaersfradragGrunnlag(it) },
      lopendeBidragGrunnlagListe = resultatGrunnlag.lopendeBidragListe.map { LopendeBidragGrunnlag(it) }
  )
}

@ApiModel(value = "Grunnlaget for beregning - bidragsevne")
data class BidragsevneGrunnlag(
    @ApiModelProperty(value = "Bidragsevne beløp") var bidragsevneBelop: BigDecimal = BigDecimal.ZERO
) {

  constructor(bidragsevne: BidragsevneCore) : this(
      bidragsevneBelop = bidragsevne.bidragsevneBelop
  )
}

@ApiModel(value = "Grunnlaget for beregning - BPs andel særtilskudd")
data class BPAndelSaertilskuddGrunnlag(
    @ApiModelProperty(value = "BPs andel særtilskudd prosent") var bpAndelSaertilskuddProsent: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "BPs andel særtilskudd beløp") var bpAndelSaertilskuddBelop: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Barnet er selvforsørget") var barnetErSelvforsorget: Boolean = false
) {

  constructor(bpAndelSaertilskudd: BPsAndelSaertilskuddCore) : this(
      bpAndelSaertilskuddProsent = bpAndelSaertilskudd.bPsAndelSaertilskuddProsent,
      bpAndelSaertilskuddBelop = bpAndelSaertilskudd.bPsAndelSaertilskuddBelop,
      barnetErSelvforsorget = bpAndelSaertilskudd.barnetErSelvforsorget
  )
}

@ApiModel(value = "Grunnlaget for beregning - samværsfradrag")
data class SamvaersfradragGrunnlag(
    @ApiModelProperty(value = "Barn person-id") var samvaersfradragBarnPersonId: Int? = null,
    @ApiModelProperty(value = "Samværsfradrag beløp") var samvaersfradragBelop: BigDecimal = BigDecimal.ZERO
) {

  constructor(samvaersfradrag: SamvaersfradragCore) : this(
      samvaersfradragBarnPersonId = samvaersfradrag.barnPersonId,
      samvaersfradragBelop = samvaersfradrag.samvaersfradragBelop
  )
}

@ApiModel(value = "Grunnlaget for beregning - løpende bidrag")
data class LopendeBidragGrunnlag(
    @ApiModelProperty(value = "Barn person-id") var lopendeBidragBarnPersonId: Int? = null,
    @ApiModelProperty(value = "Løpende bidrag beløp") var lopendeBidragBelop: BigDecimal = BigDecimal.ZERO,
    @ApiModelProperty(value = "Opprinnelig BP andel av underholdskostnad beløp") var opprinneligBPAndelUnderholdskostnadBelop: BigDecimal? = BigDecimal.ZERO,
    @ApiModelProperty(value = "Opprinnelig samværsfradrag beløp") var opprinneligSamvaersfradragBelop: BigDecimal? = BigDecimal.ZERO,
    @ApiModelProperty(value = "Opprinnelig bidrag beløp") var opprinneligBidragBelop: BigDecimal? = BigDecimal.ZERO
) {

  constructor(lopendeBidrag: LopendeBidragCore) : this(
      lopendeBidragBarnPersonId = lopendeBidrag.barnPersonId,
      lopendeBidragBelop = lopendeBidrag.lopendeBidragBelop,
      opprinneligBPAndelUnderholdskostnadBelop = lopendeBidrag.opprinneligBPsAndelUnderholdskostnadBelop,
      opprinneligSamvaersfradragBelop = lopendeBidrag.opprinneligSamvaersfradragBelop,
      opprinneligBidragBelop = lopendeBidrag.opprinneligBidragBelop
  )
}
