package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.saertilskudd.dto.BPsAndelSaertilskuddCore
import no.nav.bidrag.beregn.saertilskudd.dto.BeregnSaertilskuddResultatCore
import no.nav.bidrag.beregn.saertilskudd.dto.BidragsevneCore
import no.nav.bidrag.beregn.saertilskudd.dto.LopendeBidragCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.saertilskudd.dto.ResultatPeriodeCore
import no.nav.bidrag.beregn.saertilskudd.dto.SamvaersfradragCore
import java.math.BigDecimal

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
