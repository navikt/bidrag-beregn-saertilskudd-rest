package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
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
@Schema(description = "Grunnlaget for en beregning av særtilskudd")
data class BeregnSaertilskuddGrunnlag(
        @Schema(description = "Periodisert liste over bidragspliktiges løpende bidrag")
        val lopendeBidragBPPeriodeListe: List<LopendeBidragBPPeriode>? = null
)

@Schema(description = "Løpende bidrag bidragspliktig")
data class LopendeBidragBPPeriode(
        @Schema(description = "Løpende bidrag fra-til-dato") var lopendeBidragDatoFraTil: Periode? = null,
        @Schema(description = "Barn person-id") var lopendeBidragBarnPersonId: Int? = null,
        @Schema(description = "Løpende bidrag beløp") var lopendeBidragBelop: BigDecimal? = null,
        @Schema(description = "Opprinnelig BP andel av underholdskostnad beløp") var opprinneligBPAndelUnderholdskostnadBelop: BigDecimal? = null,
        @Schema(description = "Opprinnelig samværsfradrag beløp") var opprinneligSamvaersfradragBelop: BigDecimal? = null,
        @Schema(description = "Opprinnelig bidrag beløp") var opprinneligBidragBelop: BigDecimal? = null
) {

    fun tilCore() = LopendeBidragPeriodeCore(
            referanse = "",
            periodeDatoFraTil = if (lopendeBidragDatoFraTil != null) lopendeBidragDatoFraTil!!.tilCore(
                    "lopendeBidrag"
            ) else throw UgyldigInputException("lopendeBidragDatoFraTil kan ikke være null"),
            barnPersonId = if (lopendeBidragBarnPersonId != null) lopendeBidragBarnPersonId!! else throw UgyldigInputException(
                    "lopendeBidragBarnPersonId kan ikke være null"
            ),
            lopendeBidragBelop = if (lopendeBidragBelop != null) lopendeBidragBelop!! else throw UgyldigInputException(
                    "lopendeBidragBelop kan ikke være null"
            ),
            opprinneligBPsAndelUnderholdskostnadBelop = if (opprinneligBPAndelUnderholdskostnadBelop != null) opprinneligBPAndelUnderholdskostnadBelop!!
            else throw UgyldigInputException("opprinneligBPAndelUnderholdskostnadBelop kan ikke være null"),
            opprinneligSamvaersfradragBelop = if (opprinneligSamvaersfradragBelop != null) opprinneligSamvaersfradragBelop!!
            else throw UgyldigInputException("opprinneligSamvaersfradragBelop kan ikke være null"),
            opprinneligBidragBelop = if (opprinneligBidragBelop != null) opprinneligBidragBelop!! else throw UgyldigInputException(
                    "opprinneligBidragBelop kan ikke være null"
            )
    )
}


// Resultat
@Schema(description = "Resultatet av en beregning av særtilskudd")
data class BeregnSaertilskuddResultat(
        @Schema(description = "Periodisert liste over resultat av beregning av særtilskudd") var resultatPeriodeListe: List<ResultatPeriodeSaertilskudd> = emptyList()
) {

    constructor(beregnSaertilskuddResultat: BeregnSaertilskuddResultatCore) : this(
            resultatPeriodeListe = beregnSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriodeSaertilskudd(it) }
    )
}

@Schema(description = "Resultatet av beregning av særtilskudd for en gitt periode")
data class ResultatPeriodeSaertilskudd(
        @Schema(description = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
        @Schema(description = "Beregning resultat innhold liste") var resultatBeregning: ResultatBeregningSaertilskudd = ResultatBeregningSaertilskudd(),
        @Schema(description = "Beregning grunnlag innhold") var resultatGrunnlag: List<String> = emptyList()
) {

    constructor(resultatPeriode: ResultatPeriodeCore) : this(
            resultatDatoFraTil = Periode(resultatPeriode.periode),
            resultatBeregning = ResultatBeregningSaertilskudd(resultatPeriode.resultatListe),
            resultatGrunnlag = resultatPeriode.grunnlagReferanseListe
    )
}

@Schema(description = "Resultatet av beregning av særtilskudd")
data class ResultatBeregningSaertilskudd(
        @Schema(description = "Beløp særtilskudd") var resultatBelop: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Resultatkode særtilskudd") var resultatKode: String = ""
) {

    constructor(resultatBeregning: ResultatBeregningCore) : this(
            resultatBelop = resultatBeregning.belop,
            resultatKode = resultatBeregning.kode
    )
}

@Schema(description = "Grunnlaget for beregning av særtilskudd")
data class ResultatGrunnlagSaertilskudd(
        @Schema(description = "Bidragsevne") var bidragsevneGrunnlag: BidragsevneGrunnlag = BidragsevneGrunnlag(),
        @Schema(description = "BPs andel særtilskudd") var bpAndelSaertilskuddGrunnlag: BPAndelSaertilskuddGrunnlag = BPAndelSaertilskuddGrunnlag(),
        @Schema(description = "Liste over samværsfradrag") var samvaersfradragGrunnlagListe: List<SamvaersfradragGrunnlag> = emptyList(),
        @Schema(description = "Liste over løpende bidrag") var lopendeBidragGrunnlagListe: List<LopendeBidragGrunnlag> = emptyList()
) {

    constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
            bidragsevneGrunnlag = BidragsevneGrunnlag(resultatGrunnlag.bidragsevne),
            bpAndelSaertilskuddGrunnlag = BPAndelSaertilskuddGrunnlag(resultatGrunnlag.bPsAndelSaertilskudd),
            samvaersfradragGrunnlagListe = resultatGrunnlag.samvaersfradragListe.map { SamvaersfradragGrunnlag(it) },
            lopendeBidragGrunnlagListe = resultatGrunnlag.lopendeBidragListe.map { LopendeBidragGrunnlag(it) }
    )
}

@Schema(description = "Grunnlaget for beregning - bidragsevne")
data class BidragsevneGrunnlag(
        @Schema(description = "Bidragsevne beløp") var bidragsevneBelop: BigDecimal = BigDecimal.ZERO
) {

    constructor(bidragsevne: BidragsevneCore) : this(
            bidragsevneBelop = bidragsevne.bidragsevneBelop
    )
}

@Schema(description = "Grunnlaget for beregning - BPs andel særtilskudd")
data class BPAndelSaertilskuddGrunnlag(
        @Schema(description = "BPs andel særtilskudd prosent") var bpAndelSaertilskuddProsent: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "BPs andel særtilskudd beløp") var bpAndelSaertilskuddBelop: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Barnet er selvforsørget") var barnetErSelvforsorget: Boolean = false
) {

    constructor(bpAndelSaertilskudd: BPsAndelSaertilskuddCore) : this(
            bpAndelSaertilskuddProsent = bpAndelSaertilskudd.bPsAndelSaertilskuddProsent,
            bpAndelSaertilskuddBelop = bpAndelSaertilskudd.bPsAndelSaertilskuddBelop,
            barnetErSelvforsorget = bpAndelSaertilskudd.barnetErSelvforsorget
    )
}

@Schema(description = "Grunnlaget for beregning - samværsfradrag")
data class SamvaersfradragGrunnlag(
        @Schema(description = "Barn person-id") var samvaersfradragBarnPersonId: Int? = null,
        @Schema(description = "Samværsfradrag beløp") var samvaersfradragBelop: BigDecimal = BigDecimal.ZERO
) {

    constructor(samvaersfradrag: SamvaersfradragCore) : this(
            samvaersfradragBarnPersonId = samvaersfradrag.barnPersonId,
            samvaersfradragBelop = samvaersfradrag.samvaersfradragBelop
    )
}

@Schema(description = "Grunnlaget for beregning - løpende bidrag")
data class LopendeBidragGrunnlag(
        @Schema(description = "Barn person-id") var lopendeBidragBarnPersonId: Int? = null,
        @Schema(description = "Løpende bidrag beløp") var lopendeBidragBelop: BigDecimal = BigDecimal.ZERO,
        @Schema(description = "Opprinnelig BP andel av underholdskostnad beløp") var opprinneligBPAndelUnderholdskostnadBelop: BigDecimal? = BigDecimal.ZERO,
        @Schema(description = "Opprinnelig samværsfradrag beløp") var opprinneligSamvaersfradragBelop: BigDecimal? = BigDecimal.ZERO,
        @Schema(description = "Opprinnelig bidrag beløp") var opprinneligBidragBelop: BigDecimal? = BigDecimal.ZERO
) {

    constructor(lopendeBidrag: LopendeBidragCore) : this(
            lopendeBidragBarnPersonId = lopendeBidrag.barnPersonId,
            lopendeBidragBelop = lopendeBidrag.lopendeBidragBelop,
            opprinneligBPAndelUnderholdskostnadBelop = lopendeBidrag.opprinneligBPsAndelUnderholdskostnadBelop,
            opprinneligSamvaersfradragBelop = lopendeBidrag.opprinneligSamvaersfradragBelop,
            opprinneligBidragBelop = lopendeBidrag.opprinneligBidragBelop
    )
}
