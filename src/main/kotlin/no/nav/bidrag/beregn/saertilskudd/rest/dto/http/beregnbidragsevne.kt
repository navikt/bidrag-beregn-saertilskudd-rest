package no.nav.bidrag.beregn.saertilskudd.rest.dto.http

import io.swagger.v3.oas.annotations.media.Schema
import no.nav.bidrag.beregn.bidragsevne.dto.BeregnBidragsevneResultatCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatBeregningCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatGrunnlagCore
import no.nav.bidrag.beregn.bidragsevne.dto.ResultatPeriodeCore
import java.math.BigDecimal

// Resultat
@Schema(description = "Resultatet av en bidragsevnesberegning for bidragspliktig")
data class BeregnBPBidragsevneResultat(
    @Schema(description = "Periodisert liste over resultat av bidragsevnesberegning") var resultatPeriodeListe: List<ResultatPeriodeBidragsevne> = emptyList()
) {

  constructor(beregnBidragsevneResultat: BeregnBidragsevneResultatCore) : this(
      resultatPeriodeListe = beregnBidragsevneResultat.resultatPeriodeListe.map { ResultatPeriodeBidragsevne(it) }
  )
}

@Schema(description = "Resultatet av en beregning for en gitt periode")
data class ResultatPeriodeBidragsevne(
    @Schema(description = "Beregning resultat fra-til-dato") var resultatDatoFraTil: Periode = Periode(),
    @Schema(description = "Beregning resultat innhold") var resultatBeregning: ResultatBeregningBidragsevne = ResultatBeregningBidragsevne(),
    @Schema(description = "Beregning grunnlag innhold") var grunnlagReferanseListe: List<String> = emptyList()
) {

  constructor(resultatPeriode: ResultatPeriodeCore) : this(
      resultatDatoFraTil = Periode(resultatPeriode.periode),
      resultatBeregning = ResultatBeregningBidragsevne(resultatPeriode.resultatBeregning),
      grunnlagReferanseListe = resultatPeriode.grunnlagReferanseListe
  )
}

@Schema(description = "Resultatet av en beregning")
data class ResultatBeregningBidragsevne(
    @Schema(description = "Resultatevne beløp") var resultatEvneBelop: BigDecimal = BigDecimal.ZERO
) {

  constructor(resultatBeregning: ResultatBeregningCore) : this(
      resultatEvneBelop = resultatBeregning.resultatEvneBelop
  )
}

@Schema(description = "Grunnlaget for en beregning")
data class ResultatGrunnlagBidragsevne(
    @Schema(description = "Liste over bidragspliktiges inntekter") var inntektListe: List<Inntekt> = emptyList(),
    @Schema(description = "Bidragspliktiges skatteklasse") var skatteklasse: Int = 0,
    @Schema(description = "Bidragspliktiges bostatuskode") var bostatusKode: String = "",
    @Schema(description = "Antall egne barn i bidragspliktiges husstand") var antallEgneBarnIHusstand: BigDecimal = BigDecimal.ZERO,
    @Schema(description = "Bidragspliktiges særfradragkode") var saerfradragKode: String = "",
    @Schema(description = "Liste over sjablonperioder") var sjablonListe: List<Sjablon> = emptyList()
) {

  constructor(resultatGrunnlag: ResultatGrunnlagCore) : this(
      inntektListe = resultatGrunnlag.inntektListe.map { Inntekt(it) },
      skatteklasse = resultatGrunnlag.skatteklasse,
      bostatusKode = resultatGrunnlag.bostatusKode,
      antallEgneBarnIHusstand = resultatGrunnlag.antallEgneBarnIHusstand,
      saerfradragKode = resultatGrunnlag.saerfradragkode,
      sjablonListe = resultatGrunnlag.sjablonListe.map { Sjablon(it) }
  )
}
