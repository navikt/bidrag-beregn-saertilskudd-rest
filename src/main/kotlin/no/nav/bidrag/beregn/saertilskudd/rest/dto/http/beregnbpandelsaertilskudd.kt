package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.BeregnBPsAndelSaertilskuddResultatCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.bpsandelsaertilskudd.dto.ResultatPeriodeCore
import java.math.BigDecimal

// Resultat
@Schema(description = "Resultatet av en beregning av bidragspliktiges andel av særtilskudd")
data class BeregnBPAndelSaertilskuddResultat(
    @Schema(description = "Periodisert liste over resultat av beregning av bidragspliktiges andel av særtilskudd")
    var resultatPeriodeListe: List<ResultatPeriodeBPAndelSaertilskudd> = emptyList()
) {

  constructor(beregnBPAndelSaertilskuddResultat: BeregnBPsAndelSaertilskuddResultatCore) : this(
      resultatPeriodeListe = beregnBPAndelSaertilskuddResultat.resultatPeriodeListe.map { ResultatPeriodeBPAndelSaertilskudd(it) }
  )
}

@Schema(description = "Resultatet av beregning av bidragspliktiges andel av særtilskudd for en gitt periode")
data class ResultatPeriodeBPAndelSaertilskudd(
    @Schema(description = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
    @Schema(description = "Beregning resultat innhold") var resultatBeregning: ResultatBeregningBPAndelSaertilskudd = ResultatBeregningBPAndelSaertilskudd(),
    @Schema(description = "Beregning grunnlag innhold") var resultatGrunnlag: ResultatGrunnlagBPAndelSaertilskudd = ResultatGrunnlagBPAndelSaertilskudd()
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.resultatDatoFraTil),
      resultatBeregning = ResultatBeregningBPAndelSaertilskudd(resultatPeriode.resultatBeregning),
      resultatGrunnlag = ResultatGrunnlagBPAndelSaertilskudd(resultatPeriode.resultatGrunnlag)
  )
}

@Schema(description = "Resultatet av beregning av bidragspliktiges andel av særtilskudd")
data class ResultatBeregningBPAndelSaertilskudd(
    @Schema(description = "Resultatandel prosent") var resultatAndelProsent: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Resultatandel beløp") var resultatAndelBelop: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Barnet er selvforsørget") var barnetErSelvforsorget: Boolean = false
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatAndelProsent = resultatBeregning.resultatAndelProsent,
      resultatAndelBelop = resultatBeregning.resultatAndelBelop,
      barnetErSelvforsorget = resultatBeregning.barnetErSelvforsorget
  )
}

@Schema(description = "Grunnlaget for beregning av bidragspliktiges andel av særtilskudd")
data class ResultatGrunnlagBPAndelSaertilskudd(
    @Schema(description = "Netto særtilskudd beløp") var nettoSaertilskuddBelop: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Liste over bidragspliktiges inntekter") var inntektBPListe: List<Inntekt> = emptyList(),
    @Schema(description = "Liste over bidragsmottakers inntekter") var inntektBMListe: List<InntektBM> = emptyList(),
    @Schema(description = "Liste over søknadsbarnets inntekter") var inntektSBListe: List<Inntekt> = emptyList(),
    @Schema(description = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      nettoSaertilskuddBelop = resultatGrunnlag.nettoSaertilskuddBelop,
      inntektBPListe = resultatGrunnlag.inntektBPListe.map { Inntekt(it) },
      inntektBMListe = resultatGrunnlag.inntektBMListe.map { InntektBM(it) },
      inntektSBListe = resultatGrunnlag.inntektBBListe.map { Inntekt(it) },
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}
